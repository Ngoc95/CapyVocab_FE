package com.example.capyvocab_fe.admin.topic.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure

interface AdminTopicRepository {
    suspend fun getTopicsByCourse(courseId: Int): Either<AdminFailure, List<Topic>>
}