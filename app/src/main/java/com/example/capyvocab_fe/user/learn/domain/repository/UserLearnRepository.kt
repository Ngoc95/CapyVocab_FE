package com.example.capyvocab_fe.user.learn.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.core.error.AppFailure

interface UserLearnRepository {
    suspend fun getAllCourses(page: Int = 1, title: String? = null, level: String? = null): Either<AppFailure, List<Course>>
    suspend fun getCourseTopics(id: Int, page: Int, title: String? = null): Either<AppFailure, List<Topic>>
    suspend fun getCourseById(id: Int): Either<AppFailure, Course>
    suspend fun getTopicWords(id: Int): Either<AppFailure, List<Word>>
    suspend fun getTopicById(id: Int): Either<AppFailure, Topic>
    suspend fun markTopicComplete(topicId: Int): Either<AppFailure, Unit>
}