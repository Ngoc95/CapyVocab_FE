package com.example.capyvocab_fe.auth.data.repository

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.example.capyvocab_fe.auth.data.mapper.toAuthFailure
import com.example.capyvocab_fe.auth.data.mapper.toDomain
import com.example.capyvocab_fe.auth.data.remote.AuthApi
import com.example.capyvocab_fe.auth.data.remote.model.LoginRequest
import com.example.capyvocab_fe.auth.domain.error.ApiError
import com.example.capyvocab_fe.auth.domain.error.AuthFailure
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.auth.domain.repository.AuthRepository
import com.example.capyvocab_fe.core.data.TokenManager
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Either<AuthFailure, User> {
        return Either.catch {
            val response = authApi.login(LoginRequest(username, password))

            // Lưu accessToken và refreshToken vào DataStore
            tokenManager.saveTokens(
                accessToken = response.metaData.accessToken,
                refreshToken = response.metaData.refreshToken
            )

            response.metaData.user.toDomain()
        }.mapLeft {
            it.toAuthFailure()
        }
    }


    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun getUserInfo(): Either<AuthFailure, User?> {
        TODO("Not yet implemented")
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String,
    ): Either<AuthFailure, User> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken(refreshToken: String): Either<AuthFailure, Pair<String, String>> {
        return Either.catch {
            authApi.refreshToken(mapOf("refreshToken" to refreshToken))
        }.mapLeft {
            it.toAuthFailure()
        }.flatMap { response ->
            val accessToken = response.metaData["accessToken"]
            val newRefreshToken = response.metaData["refreshToken"]

            if (accessToken != null && newRefreshToken != null) {
                Either.Right(Pair(accessToken, newRefreshToken))
            } else {
                Either.Left(
                    AuthFailure(
                        error = ApiError.UnknownResponse,
                        message = "Missing access or refresh token"
                    )
                )
            }

        }
    }
}