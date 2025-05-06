package com.example.capyvocab_fe.admin.course.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.course.data.remote.AdminCourseApi
import com.example.capyvocab_fe.admin.course.data.remote.model.CreateCourseReq
import com.example.capyvocab_fe.admin.course.data.remote.model.UpdateCourseReq
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.domain.model.CourseWithTopics
import com.example.capyvocab_fe.admin.course.domain.repository.AdminCourseRepository
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.user.domain.error.toAdminFailure
import javax.inject.Inject

class AdminCourseRepositoryImpl @Inject constructor(
    private val api: AdminCourseApi
) : AdminCourseRepository {
    override suspend fun getAllCourses(page: Int): Either<AdminFailure, List<Course>> {
        return Either.catch {
            val response = api.getAllCourses(page)
            response.metaData.courses
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun getCourseById(id: Int): Either<AdminFailure, CourseWithTopics> {
        return Either.catch {
            val response = api.getCourseById(id)
            response.metaData
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun createCourse(courseRequest: CreateCourseReq): Either<AdminFailure, Course> {
        return Either.catch {
            val response = api.createCourse(courseRequest)
            response.metaData
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun updateCourse(
        id: Int,
        courseRequest: UpdateCourseReq
    ): Either<AdminFailure, Course> {
        return Either.catch {
            val response = api.updateCourse(id, courseRequest)
            response.metaData
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun deleteCourse(id: Int): Either<AdminFailure, Unit> {
        return Either.catch {
            api.deleteCourse(id)
            Unit
        }.mapLeft { it.toAdminFailure() }
    }


}

