package com.example.capyvocab_fe.auth.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.auth.domain.error.AuthFailure
import com.example.capyvocab_fe.auth.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Either<AuthFailure, User>
    suspend fun getUserInfo(): Either<AuthFailure, User?>
    suspend fun register(email: String, username: String, password: String): Either<AuthFailure, User>
    suspend fun refreshToken(refreshToken: String): Either<AuthFailure, Pair<String, String>>
    suspend fun sendVerificationEmail(): Either<AuthFailure, Unit>
    suspend fun verifyEmail(code: Int): Either<AuthFailure, Unit>
    suspend fun googleLogin(token: String): Either<AuthFailure, User>
    suspend fun sendChangePasswordEmail(email: String): Either<AuthFailure, Unit>
    suspend fun changePassword(email: String, code: String, newPassword: String): Either<AuthFailure, Unit>
}