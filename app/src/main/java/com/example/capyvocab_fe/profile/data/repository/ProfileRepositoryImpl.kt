package com.example.capyvocab_fe.profile.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import arrow.core.Either
import com.example.capyvocab_fe.admin.user.data.mapper.toDomain
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.auth.data.mapper.toAuthFailure
import com.example.capyvocab_fe.auth.domain.error.AuthFailure
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.profile.data.remote.ProfileApi
import com.example.capyvocab_fe.profile.data.remote.model.UpdatePasswordRequest
import com.example.capyvocab_fe.profile.data.remote.model.UpdateProfileRequest
import com.example.capyvocab_fe.profile.domain.model.UserProfile
import com.example.capyvocab_fe.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi
) : ProfileRepository {
    override suspend fun getProfile(): Either<AuthFailure, UserProfile?> {
        return Either.catch {
            val response = api.getProfile()
            response.metaData.user
        }.mapLeft {
            it.toAuthFailure()
        }
    }

    override suspend fun updateProfile(userId: Int, avatar: String?, email: String, username: String): Either<AuthFailure, UserProfile> {
        return Either.catch {
            val body = UpdateProfileRequest(
                avatar = avatar,
                email = email,
                username = username
            )
            api.updateProfile(userId, body).metaData
        }.mapLeft { it.toAuthFailure() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updatePassword(userId: Int, oldPassword: String?, newPassword: String?): Either<AppFailure, User> {
        return Either.catch {
            val request = UpdatePasswordRequest(
                oldPassword = oldPassword,
                newPassword = newPassword
            )
            val response = api.updatePassword(userId, request)
            response.metaData.toDomain()
        }.mapLeft { it.toAppFailure() }
    }
}
