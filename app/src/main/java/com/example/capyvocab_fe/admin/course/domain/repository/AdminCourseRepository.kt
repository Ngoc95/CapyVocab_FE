package com.example.capyvocab_fe.admin.course.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure

interface AdminCourseRepository {
    suspend fun getAllCourses(): Either<AdminFailure, List<Course>>
}