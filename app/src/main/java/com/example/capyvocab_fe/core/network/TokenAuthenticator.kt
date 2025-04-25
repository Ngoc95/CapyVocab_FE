package com.example.capyvocab_fe.core.network

import android.util.Log
import com.example.capyvocab_fe.auth.data.remote.AuthApi
import com.example.capyvocab_fe.auth.domain.error.ApiError
import com.example.capyvocab_fe.auth.domain.repository.AuthRepository
import com.example.capyvocab_fe.core.data.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository
): Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            return null //tránh loop vô hạn
        }

        return runBlocking {
            val refreshToken = tokenManager.refreshToken.firstOrNull()

            if (refreshToken.isNullOrEmpty()) {
                Log.w("TokenAuthenticator", "No refresh token found")
                return@runBlocking null
            }

            val result = authRepository.refreshToken(refreshToken)

            result.mapLeft { failure ->
                when (failure.error) {
                    ApiError.Unauthorized -> tokenManager.clearTokens()
                    else -> Unit
                }
                Log.e("TokenAuthenticator", "Refresh token failed: ${failure.message}")
            }.getOrNull()?.let { (newAccessToken, newRefreshToken) ->
                tokenManager.saveTokens(newAccessToken, newRefreshToken)

                //Retry request with new access token
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }
}