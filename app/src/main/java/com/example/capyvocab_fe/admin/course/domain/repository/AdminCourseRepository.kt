package com.example.capyvocab_fe.admin.course.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.course.data.remote.model.CreateCourseRequest
import com.example.capyvocab_fe.admin.course.data.remote.model.UpdateCourseRequest
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure

interface AdminCourseRepository {
    suspend fun getAllCourses(page: Int = 1): Either<AdminFailure, List<Course>>

    suspend fun getCourseTopics(id: Int, page: Int): Either<AdminFailure, List<Topic>>

    suspend fun createCourse(courseRequest: CreateCourseRequest): Either<AdminFailure, Course>

    suspend fun updateCourse(id: Int, courseRequest: UpdateCourseRequest): Either<AdminFailure, Course>

    suspend fun deleteCourse(id: Int): Either<AdminFailure, Unit>

    suspend fun getCourseById(id: Int): Either<AdminFailure, Course>
}