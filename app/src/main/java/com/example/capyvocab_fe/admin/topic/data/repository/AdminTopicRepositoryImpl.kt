package com.example.capyvocab_fe.admin.topic.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.topic.data.remote.AdminTopicApi
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.topic.domain.repository.AdminTopicRepository
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.user.domain.error.toAdminFailure
import javax.inject.Inject

class AdminTopicRepositoryImpl @Inject constructor(
    private val api: AdminTopicApi
) : AdminTopicRepository {

    override suspend fun getTopicsByCourse(courseId: Int): Either<AdminFailure, List<Topic>> {
        return Either.catch {
            val response = api.getTopicsByCourse(courseId)
            response.metaData ?: emptyList()
        }.mapLeft { it.toAdminFailure() }
    }
}
