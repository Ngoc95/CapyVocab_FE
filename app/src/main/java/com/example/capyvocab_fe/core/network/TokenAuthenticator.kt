package com.example.capyvocab_fe.core.network

import android.util.Log
import com.example.capyvocab_fe.auth.data.remote.AuthApi
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
    private val authApi: AuthApi
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

            try {
                val refreshResponse = authApi.refreshToken(mapOf("refreshToken" to refreshToken))
                val newAccessToken = refreshResponse.metaData["accessToken"]
                val newRefreshToken = refreshResponse.metaData["refreshToken"]

                if (newAccessToken != null && newRefreshToken != null) {
                    // Save new tokens
                    tokenManager.saveTokens(newAccessToken, newRefreshToken)

                    // Retry the original request with new access token
                    response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                } else {
                    Log.e("TokenAuthenticator", "Missing access or refresh token in response")
                    tokenManager.clearTokens()
                    null
                }
            } catch (e: Exception) {
                Log.e("TokenAuthenticator", "Refresh token failed: ${e.message}")
                tokenManager.clearTokens()
                null
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