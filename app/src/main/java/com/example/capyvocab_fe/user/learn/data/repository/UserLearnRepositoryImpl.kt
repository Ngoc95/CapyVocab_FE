package com.example.capyvocab_fe.user.learn.data.repository


import arrow.core.Either
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.user.learn.data.remote.UserLearnApi
import com.example.capyvocab_fe.user.learn.data.remote.model.CompleteTopicRequest
import com.example.capyvocab_fe.user.learn.domain.repository.UserLearnRepository
import javax.inject.Inject

class UserLearnRepositoryImpl @Inject constructor(
    private val api: UserLearnApi
) : UserLearnRepository {
    override suspend fun getAllCourses(page: Int, title: String?): Either<AppFailure, List<Course>> {
        return Either.catch {
            val response = api.getAllCourses(page, title = title)
            response.metaData.courses
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getCourseTopics(id: Int, page: Int, title: String?): Either<AppFailure, List<Topic>> {
        return Either.catch {
            val response = api.getCourseTopics(id, page, title = title)
            response.metaData.topics
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getCourseById(id: Int): Either<AppFailure, Course> {
        return Either.catch {
            api.getCourseById(id).metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getTopicWords(id: Int): Either<AppFailure, List<Word>> {
        return Either.catch {
            api.getTopicWords(id).metaData.words
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getTopicById(id: Int): Either<AppFailure, Topic> {
        return Either.catch {
            api.getTopicById(id).metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun markTopicComplete(topicId: Int): Either<AppFailure, Unit> {
        return Either.catch {
            api.markTopicComplete(CompleteTopicRequest(topicId))
        }.mapLeft { throwable ->
            throwable.toAppFailure()
        }.map { }
    }
}