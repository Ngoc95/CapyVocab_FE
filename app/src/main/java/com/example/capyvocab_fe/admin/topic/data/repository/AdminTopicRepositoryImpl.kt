package com.example.capyvocab_fe.admin.topic.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.topic.data.remote.AdminTopicApi
import com.example.capyvocab_fe.admin.topic.data.remote.model.CreateTopicReq
import com.example.capyvocab_fe.admin.topic.data.remote.model.UpdateTopicReq
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.topic.domain.repository.AdminTopicRepository
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.user.domain.error.toAdminFailure
import javax.inject.Inject

class AdminTopicRepositoryImpl @Inject constructor(
    private val api: AdminTopicApi
) : AdminTopicRepository {
    override suspend fun updateTopic(
        id: Int,
        topicRequest: UpdateTopicReq
    ): Either<AdminFailure, Topic> {
        return Either.catch {
            api.updateTopic(id, topicRequest).metaData
        }.mapLeft {
            it.toAdminFailure()
        }
    }

    override suspend fun deleteTopic(id: Int): Either<AdminFailure, Unit> {
        return Either.catch {
            api.deleteTopic(id)
            Unit
        }.mapLeft {
            it.toAdminFailure()
        }
    }

    override suspend fun createTopic(topicRequest: CreateTopicReq): Either<AdminFailure, Topic> {
        return Either.catch {
            api.createTopic(topicRequest).metaData
        }.mapLeft {
            it.toAdminFailure()
        }
    }

    override suspend fun getTopicById(id: Int): Either<AdminFailure, Topic> {
        return Either.catch {
            api.getTopicById(id).metaData
        }.mapLeft {
            it.toAdminFailure()
        }
    }


}
