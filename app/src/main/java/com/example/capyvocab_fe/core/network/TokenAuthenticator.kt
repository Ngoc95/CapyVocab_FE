package com.example.capyvocab_fe.core.network

import com.example.capyvocab_fe.auth.data.remote.AuthApi
import com.example.capyvocab_fe.core.data.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

//class TokenAuthenticator @Inject constructor(
//    private val tokenManager: TokenManager,
//    private val authApi: AuthApi
//): Authenticator {
//
//    override fun authenticate(route: Route?, response: Response): Request? {
//        if(responseCount(response) >= 3) return null
//
//        val refreshToken = runBlocking {
//            tokenManager.refreshToken.firstOrNull()
//        } ?: return null
//
//        val newTokens = try {
//            val refreshResponse = authApi.refreshToken(RefreshTokenRequest(refreshToken))
//            if (refreshResponse.isSuccessful)
//        }
//    }
//}