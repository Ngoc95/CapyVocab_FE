package com.example.capyvocab_fe.admin.user.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.user.domain.model.User

interface AdminUserRepository {
    suspend fun getAllUsers(): Either<AdminFailure, List<User>>
    suspend fun createUser(user: User): Either<AdminFailure, User>
    suspend fun updateUser(user: User): Either<AdminFailure, User>
}