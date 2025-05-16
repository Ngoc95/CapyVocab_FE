package com.example.capyvocab_fe.admin.word.data.repository

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.MyApplication
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

    override suspend fun createWords(createWordRequest: CreateWordRequest): Either<AppFailure, List<Word>> {
        return Either.catch {
            adminWordApi.createWords(createWordRequest)
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getAllWords(page: Int): Either<AppFailure, List<Word>> {
        return Either.catch {
            adminWordApi.getAllWords(page).metaData.words
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getWordById(id: Int): Either<AppFailure, Word> {
        return Either.catch {
            adminWordApi.getWordById(id)
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun updateWord(
        id: Int,
        updateWordRequest: UpdateWordRequest
    ): Either<AppFailure, Word> {
        return Either.catch {
            adminWordApi.updateWord(id, updateWordRequest)
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun deleteWord(id: Int): Either<AppFailure, DeleteResponse> {
        return Either.catch {
            adminWordApi.deleteWordById(id)
        }.mapLeft { it.toAppFailure() }
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