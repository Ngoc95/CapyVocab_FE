package com.example.capyvocab_fe.admin.course.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.course.data.remote.AdminCourseApi
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.domain.repository.AdminCourseRepository
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.user.domain.error.toAdminFailure
import javax.inject.Inject

class AdminCourseRepositoryImpl @Inject constructor(
    private val api: AdminCourseApi
) : AdminCourseRepository {

    override suspend fun getAllCourses(): Either<AdminFailure, List<Course>> {
        return Either.catch {
            val response = api.getAllCourses()
            response.metaData ?: emptyList()
        }.mapLeft { it.toAdminFailure() }
    }
}

