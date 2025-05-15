package com.example.capyvocab_fe.admin.word.data.repository

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.MyApplication
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.user.domain.error.toAdminFailure
import com.example.capyvocab_fe.admin.word.data.remote.AdminWordApi
import com.example.capyvocab_fe.admin.word.data.remote.model.CreateWordRequest
import com.example.capyvocab_fe.admin.word.data.remote.model.UpdateWordRequest
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.admin.word.domain.repository.AdminWordRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

class AdminWordRepositoryImpl @Inject constructor(
    private val adminWordApi: AdminWordApi
) : AdminWordRepository {

    override suspend fun createWords(createWordRequest: CreateWordRequest): Either<AdminFailure, List<Word>> {
        return Either.catch {
            adminWordApi.createWords(createWordRequest).metaData
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun getAllWords(page: Int): Either<AdminFailure, List<Word>> {
        return Either.catch {
            adminWordApi.getAllWords(page).metaData.words
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun getWordById(id: Int): Either<AdminFailure, Word> {
        return Either.catch {
            adminWordApi.getWordById(id).metaData
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun updateWord(
        id: Int,
        updateWordRequest: UpdateWordRequest
    ): Either<AdminFailure, Word> {
        return Either.catch {
            adminWordApi.updateWord(id, updateWordRequest).metaData
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun deleteWord(id: Int): Either<AdminFailure, Unit> {
        return Either.catch {
            adminWordApi.deleteWordById(id)
            Unit
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun uploadImage(uri: Uri): Either<AdminFailure, String> {
        return Either.catch {
            val contentResolver = MyApplication.instance.contentResolver
            val inputStream =
                contentResolver.openInputStream(uri) ?: throw IOException("Không mở được ảnh")
            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val requestBody = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())

            val multipart = MultipartBody.Part.createFormData("IMAGE", fileName, requestBody)
            val typePart = "IMAGE".toRequestBody("text/plain".toMediaTypeOrNull())

            val response = adminWordApi.uploadImage(typePart, multipart)
            response.metaData.firstOrNull()?.destination
                ?: throw IOException("Không nhận được URL ảnh")
        }.mapLeft {
            it.toAdminFailure()
        }
    }
}