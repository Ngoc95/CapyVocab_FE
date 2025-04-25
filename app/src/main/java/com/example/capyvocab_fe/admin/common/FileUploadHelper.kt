package com.example.capyvocab_fe.admin.common

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.MyApplication
import com.example.capyvocab_fe.admin.user.data.remote.model.ApiResponse
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.user.domain.error.toAdminFailure
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

suspend fun uploadFile(
    uri: Uri,
    fileType: String,
    formFieldName: String,
    filePrefix: String,
    uploadCall: suspend (RequestBody, MultipartBody.Part) -> UploadResponse
): Either<AdminFailure, String> {
    return Either.catch {

        val contentResolver = MyApplication.instance.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: throw IOException("Không mở được file")
        val fileName = "${filePrefix}_${System.currentTimeMillis()}.${fileType.substringAfter('/')}"
        val requestBody = inputStream.readBytes().toRequestBody(fileType.toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(formFieldName, fileName, requestBody)
        val typePart = formFieldName.toRequestBody("text/plain".toMediaType())

        val response = uploadCall(typePart, filePart)
        response.metaData.firstOrNull()?.destination ?: throw IOException("Không nhận được URL")
    }.mapLeft { it.toAdminFailure() }
}

data class UploadResult(
    val fileName: String,
    val destination: String
)

typealias UploadResponse = ApiResponse<List<UploadResult>>