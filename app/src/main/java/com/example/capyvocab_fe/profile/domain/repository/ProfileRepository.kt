package com.example.capyvocab_fe.profile.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.auth.domain.error.AuthFailure
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): Either<AuthFailure, UserProfile?>
    suspend fun logout(): Either<AuthFailure, Unit>
    suspend fun updateProfile(userId: Int, avatar: String?, email: String, username: String): Either<AuthFailure, UserProfile>
    suspend fun updatePassword(userId: Int, oldPassword: String?, newPassword: String?): Either<AppFailure, User>
}