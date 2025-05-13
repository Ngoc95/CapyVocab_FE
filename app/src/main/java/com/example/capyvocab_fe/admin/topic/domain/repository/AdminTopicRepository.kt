package com.example.capyvocab_fe.admin.topic.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.topic.data.remote.model.CreateTopicRequest
import com.example.capyvocab_fe.admin.topic.data.remote.model.UpdateTopicRequest
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.word.domain.model.Word

interface AdminTopicRepository {
    suspend fun getAllTopic(page: Int = 1): Either<AdminFailure, List<Topic>>
    suspend fun updateTopic(id: Int, topicRequest: UpdateTopicRequest): Either<AdminFailure, Topic>
    suspend fun deleteTopic(id: Int): Either<AdminFailure, Unit>
    suspend fun createTopic(topicRequest: CreateTopicRequest): Either<AdminFailure, List<Topic>>
    suspend fun getTopicWords(id: Int, page: Int): Either<AdminFailure, List<Word>>
    suspend fun getTopicById(id: Int): Either<AdminFailure, Topic>
}