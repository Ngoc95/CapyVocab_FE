package com.example.capyvocab_fe.admin.course.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.course.data.remote.model.CreateCourseRequest
import com.example.capyvocab_fe.admin.course.data.remote.model.UpdateCourseRequest
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.core.error.AppFailure

interface AdminCourseRepository {
    suspend fun getAllCourses(page: Int = 1, title: String? = null): Either<AppFailure, List<Course>>

    suspend fun getCourseTopics(courseId: Int, page: Int, title: String? = null): Either<AppFailure, List<Topic>>

    suspend fun createCourse(courseRequest: CreateCourseRequest): Either<AppFailure, Course>

    suspend fun updateCourse(
        id: Int,
        courseRequest: UpdateCourseRequest
    ): Either<AppFailure, Course>

    suspend fun deleteCourse(id: Int): Either<AppFailure, Unit>

    suspend fun getCourseById(id: Int): Either<AppFailure, Course>
}