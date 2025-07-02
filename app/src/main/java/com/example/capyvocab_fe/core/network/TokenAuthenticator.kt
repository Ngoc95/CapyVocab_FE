package com.example.capyvocab_fe.core.network

import android.util.Log
import arrow.core.Either
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
        // Prevent infinite refresh loops
        if (responseCount(response) >= 2) {
            return null
        }

        return runBlocking {
            val refreshToken = tokenManager.refreshToken.firstOrNull()

            if (refreshToken.isNullOrEmpty()) {
                Log.w("TokenAuthenticator", "No refresh token found")
                return@runBlocking null
            }
            when (val result = authRepository.refreshToken(refreshToken)) {
                is Either.Left -> {
                    Log.e("TokenAuthenticator", "Failed to refresh token: ${result.value}")
                    tokenManager.clearTokens()
                    return@runBlocking null
                }
                is Either.Right -> {
                    val (newAccessToken, newRefreshToken) = result.value
                    tokenManager.saveTokens(newAccessToken, newRefreshToken)
                    Log.i("TokenAuthenticator", "Tokens refreshed successfully")
                    response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                }
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