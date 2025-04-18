package com.example.capyvocab_fe.admin.user.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.user.data.mapper.toDomain
import com.example.capyvocab_fe.admin.user.data.mapper.toUserData
import com.example.capyvocab_fe.admin.user.data.remote.AdminUserApi
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.user.domain.error.toAdminFailure
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.admin.user.domain.repository.AdminUserRepository
import com.example.capyvocab_fe.core.data.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AdminUserRepositoryImpl @Inject constructor(
    private val adminUserApi: AdminUserApi,
    private val tokenManager: TokenManager
) : AdminUserRepository {

    override suspend fun getAllUsers(): Either<AdminFailure, List<User>> {
        return Either.catch {
            val token = tokenManager.accessToken.firstOrNull()
            val response = adminUserApi.getAllUsers("Bearer $token")
            response.metaData.users.map { it.toDomain() }
        }.mapLeft { it.toAdminFailure() }
    }


    override suspend fun createUser(user: User): Either<AdminFailure, User> {
        return Either.catch {
            adminUserApi.createUser(user.toUserData()).toDomain()
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun updateUser(user: User): Either<AdminFailure, User> {
        return Either.catch {
            adminUserApi.updateUser(user.id, user.toUserData()).toDomain()
        }.mapLeft { it.toAdminFailure() }
    }
}

