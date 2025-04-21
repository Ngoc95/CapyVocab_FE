package com.example.capyvocab_fe.admin.user.data.repository

import android.net.Uri
import androidx.compose.ui.res.stringArrayResource
import arrow.core.Either
import com.example.capyvocab_fe.MyApplication
import com.example.capyvocab_fe.admin.user.data.mapper.toDomain
import com.example.capyvocab_fe.admin.user.data.remote.AdminUserApi
import com.example.capyvocab_fe.admin.user.data.remote.model.CreateUserRequest
import com.example.capyvocab_fe.admin.user.data.remote.model.UpdateUserRequest
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.user.domain.error.toAdminFailure
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.admin.user.domain.repository.AdminUserRepository
import com.example.capyvocab_fe.core.data.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
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


    override suspend fun createUser(user: User, password: String): Either<AdminFailure, User> {
        return Either.catch {
            val token = tokenManager.accessToken.firstOrNull()
            val roleId = if (user.roleId == 0) 2 else user.roleId
            val request = CreateUserRequest(
                username = user.username,
                email = user.email,
                password = password,
                roleId = roleId,
                avatar = user.avatar
            )
            val response = adminUserApi.createUser("Bearer $token", request)
            response.metaData.toDomain()
        }.mapLeft { it.toAdminFailure() }
    }


    override suspend fun updateUser(user: User, password: String?): Either<AdminFailure, User> {
        return Either.catch {
            val token = tokenManager.accessToken.firstOrNull()
            val request = UpdateUserRequest(
                username = user.username,
                email = user.email,
               // password = password?.takeIf { it.isNotBlank() },
                roleId = user.roleId,
                status = user.status,
                avatar = user.avatar
            )
            val response = adminUserApi.updateUser("Bearer $token", user.id, request)
            response.metaData.toDomain()
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun uploadAvatarImage(uri: Uri): Either<AdminFailure, String> {
        return Either.catch {
            val token = tokenManager.accessToken.firstOrNull()

            val contentResolver = MyApplication.instance.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: throw IOException("Không mở được ảnh")
            val fileName = "avatar_${System.currentTimeMillis()}.jpg"
            val requestBody = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())

            val multipart = MultipartBody.Part.createFormData("AVATAR", fileName, requestBody)
            val typePart = "AVATAR".toRequestBody("text/plain".toMediaType())

            val response = adminUserApi.uploadAvatarImage("Bearer $token", typePart, multipart)
            response.metaData.firstOrNull()?.destination ?: throw IOException("Không nhận được URL ảnh")
        }.mapLeft { it.toAdminFailure() }
    }
}

