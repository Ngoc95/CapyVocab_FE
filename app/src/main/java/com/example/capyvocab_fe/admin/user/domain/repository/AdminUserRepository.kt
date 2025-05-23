package com.example.capyvocab_fe.admin.user.domain.repository

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.admin.user.domain.model.User

interface AdminUserRepository {
    suspend fun getAllUsers(page: Int): Either<AppFailure, List<User>>
    suspend fun createUser(user: User, password: String): Either<AppFailure, User>
    suspend fun updateUser(user: User, password: String?): Either<AppFailure, User>
    suspend fun uploadAvatarImage(uri: Uri): Either<AppFailure, String>
    suspend fun deleteUser(id: Int): Either<AppFailure, Unit>
}