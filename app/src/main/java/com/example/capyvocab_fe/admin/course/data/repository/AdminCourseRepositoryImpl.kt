package com.example.capyvocab_fe.admin.course.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.course.data.remote.AdminCourseApi
import com.example.capyvocab_fe.admin.course.data.remote.model.CreateCourseRequest
import com.example.capyvocab_fe.admin.course.data.remote.model.UpdateCourseRequest
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.domain.repository.AdminCourseRepository
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import javax.inject.Inject

class AdminCourseRepositoryImpl @Inject constructor(
    private val api: AdminCourseApi
) : AdminCourseRepository {
    override suspend fun getAllCourses(page: Int, title: String?, level: String?): Either<AppFailure, List<Course>> {
        return Either.catch {
            val response = api.getAllCourses(page, title = title, level = level)
            response.metaData.courses
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getCourseTopics(id: Int, page: Int, title: String?): Either<AppFailure, List<Topic>> {
        return Either.catch {
            val response = api.getCourseTopics(id, page, title = title)
            response.metaData.topics
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun createCourse(courseRequest: CreateCourseRequest): Either<AppFailure, Course> {
        return Either.catch {
            val response = api.createCourse(courseRequest)
            response.metaData.first()
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun updateCourse(
        id: Int,
        courseRequest: UpdateCourseRequest
    ): Either<AppFailure, Course> {
        return Either.catch {
            val response = api.updateCourse(id, courseRequest)
            response.metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun deleteCourse(id: Int): Either<AppFailure, Unit> {
        return Either.catch {
            api.deleteCourse(id)
            Unit
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getCourseById(id: Int): Either<AppFailure, Course> {
        return Either.catch {
            api.getCourseById(id).metaData
        }.mapLeft { it.toAppFailure() }
    }


}

