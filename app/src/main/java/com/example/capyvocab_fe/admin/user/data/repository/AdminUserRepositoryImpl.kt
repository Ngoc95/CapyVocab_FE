package com.example.capyvocab_fe.admin.user.data.repository

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.MyApplication
import com.example.capyvocab_fe.admin.user.data.mapper.toDomain
import com.example.capyvocab_fe.admin.user.data.remote.AdminUserApi
import com.example.capyvocab_fe.admin.user.data.remote.model.CreateUserRequest
import com.example.capyvocab_fe.admin.user.data.remote.model.UpdateUserRequest
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.admin.user.domain.repository.AdminUserRepository
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

class AdminUserRepositoryImpl @Inject constructor(
    private val adminUserApi: AdminUserApi
) : AdminUserRepository {

    override suspend fun getAllUsers(page: Int, username: String?): Either<AppFailure, List<User>> {
        return Either.catch {
            val response = adminUserApi.getAllUsers(page, username = username)
            response.metaData.users.map { it.toDomain() }
        }.mapLeft { it.toAppFailure() }
    }


    override suspend fun createUser(user: User, password: String): Either<AppFailure, User> {
        return Either.catch {
            val roleId = if (user.roleId == 0) 2 else user.roleId
            val request = CreateUserRequest(
                username = user.username,
                email = user.email,
                password = password,
                roleId = roleId,
                avatar = user.avatar
            )
            val response = adminUserApi.createUser(request)
            response.metaData.toDomain()
        }.mapLeft { it.toAppFailure() }
    }


    override suspend fun updateUser(user: User, password: String?): Either<AppFailure, User> {
        return Either.catch {
            val request = UpdateUserRequest(
                username = user.username,
                email = user.email,
                // password = password?.takeIf { it.isNotBlank() },
                roleId = user.roleId,
                status = user.status,
                avatar = user.avatar
            )
            val response = adminUserApi.updateUser(user.id, request)
            response.metaData.toDomain()
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun uploadAvatarImage(uri: Uri): Either<AppFailure, String> {
        return Either.catch {
            val contentResolver = MyApplication.instance.contentResolver
            val inputStream =
                contentResolver.openInputStream(uri) ?: throw IOException("Không mở được ảnh")
            val fileName = "avatar_${System.currentTimeMillis()}.jpg"
            val requestBody = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())

            val multipart = MultipartBody.Part.createFormData("AVATAR", fileName, requestBody)
            val typePart = "AVATAR".toRequestBody("text/plain".toMediaType())

            val response = adminUserApi.uploadAvatarImage(typePart, multipart)
            response.metaData.firstOrNull()?.destination
                ?: throw IOException("Không nhận được URL ảnh")
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun deleteUser(id: Int): Either<AppFailure, Unit> {
        return Either.catch {
            adminUserApi.deleteUser(id)
            Unit
        }.mapLeft { it.toAppFailure() }
    }
}

