package com.example.capyvocab_fe.admin.topic.data.repository

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.MyApplication
import com.example.capyvocab_fe.admin.topic.data.remote.AdminTopicApi
import com.example.capyvocab_fe.admin.topic.data.remote.model.CreateTopicRequest
import com.example.capyvocab_fe.admin.topic.data.remote.model.UpdateTopicRequest
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.topic.domain.repository.AdminTopicRepository
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.admin.word.domain.model.Word
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

class AdminTopicRepositoryImpl @Inject constructor(
    private val api: AdminTopicApi
) : AdminTopicRepository {
    override suspend fun getAllTopic(page: Int): Either<AppFailure, List<Topic>> {
        return Either.catch {
            api.getAllTopic(page).metaData.topics
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun updateTopic(
        id: Int,
        topicRequest: UpdateTopicRequest
    ): Either<AppFailure, Topic> {
        return Either.catch {
            api.updateTopic(id, topicRequest).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun deleteTopic(id: Int): Either<AppFailure, Unit> {
        return Either.catch {
            api.deleteTopic(id)
            Unit
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun createTopic(topicRequest: CreateTopicRequest): Either<AppFailure, List<Topic>> {
        return Either.catch {
            api.createTopic(topicRequest).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun getTopicWords(
        id: Int,
        page: Int
    ): Either<AppFailure, List<Word>> {
        return Either.catch {
            api.getTopicWords(id, page).metaData.words
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getTopicById(id: Int): Either<AppFailure, Topic> {
        return Either.catch {
            api.getTopicById(id).metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun uploadThumbnailImage(uri: Uri): Either<AppFailure, String> {
        return Either.catch {
            val contentResolver = MyApplication.instance.contentResolver
            val inputStream =
                contentResolver.openInputStream(uri) ?: throw IOException("Không mở được ảnh")
            val fileName = "thumbnail_${System.currentTimeMillis()}.jpg"
            val requestBody = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())

            val multipart = MultipartBody.Part.createFormData("THUMBNAIL", fileName, requestBody)
            val typePart = "THUMBNAIL".toRequestBody("text/plain".toMediaTypeOrNull())

            val response = api.uploadThumbnailImage(typePart, multipart)
            response.metaData.firstOrNull()?.destination
                ?: throw IOException("Không nhận được URL ảnh")
        }.mapLeft {
            it.toAppFailure()
        }
    }
}
