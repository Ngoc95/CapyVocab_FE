package com.example.capyvocab_fe.admin.topic.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.topic.data.remote.model.CreateTopicReq
import com.example.capyvocab_fe.admin.topic.data.remote.model.UpdateTopicReq
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure

interface AdminTopicRepository {
    suspend fun updateTopic(id: Int, topicRequest: UpdateTopicReq): Either<AdminFailure, Topic>
    suspend fun deleteTopic(id: Int): Either<AdminFailure, Unit>
    suspend fun createTopic(topicRequest: CreateTopicReq): Either<AdminFailure, Topic>

    suspend fun getTopicById(id: Int): Either<AdminFailure, Topic>
}