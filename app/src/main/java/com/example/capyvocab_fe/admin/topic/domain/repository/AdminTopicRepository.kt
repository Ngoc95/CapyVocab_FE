package com.example.capyvocab_fe.admin.topic.domain.repository

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.admin.topic.data.remote.model.CreateTopicRequest
import com.example.capyvocab_fe.admin.topic.data.remote.model.UpdateTopicRequest
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.admin.word.domain.model.Word

interface AdminTopicRepository {
    suspend fun getAllTopic(page: Int = 1): Either<AppFailure, List<Topic>>
    suspend fun updateTopic(id: Int, topicRequest: UpdateTopicRequest): Either<AppFailure, Topic>
    suspend fun deleteTopic(id: Int): Either<AppFailure, Unit>
    suspend fun createTopic(topicRequest: CreateTopicRequest): Either<AppFailure, List<Topic>>
    suspend fun getTopicWords(id: Int, page: Int): Either<AppFailure, List<Word>>
    suspend fun getTopicById(id: Int): Either<AppFailure, Topic>
    suspend fun uploadThumbnailImage(uri: Uri): Either<AdminFailure, String>
}