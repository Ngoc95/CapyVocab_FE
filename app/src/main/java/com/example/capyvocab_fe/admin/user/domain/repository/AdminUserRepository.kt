package com.example.capyvocab_fe.admin.user.domain.repository

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.user.domain.model.User

interface AdminUserRepository {
    suspend fun getAllUsers(page: Int): Either<AdminFailure, List<User>>
    suspend fun createUser(user: User, password: String): Either<AdminFailure, User>
    suspend fun updateUser(user: User, password: String?): Either<AdminFailure, User>
    suspend fun uploadAvatarImage(uri: Uri): Either<AdminFailure, String>
    suspend fun deleteUser(id: Int): Either<AdminFailure, Unit>
}