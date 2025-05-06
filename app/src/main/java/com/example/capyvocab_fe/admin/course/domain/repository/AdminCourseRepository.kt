package com.example.capyvocab_fe.admin.course.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.course.data.remote.model.CreateCourseReq
import com.example.capyvocab_fe.admin.course.data.remote.model.UpdateCourseReq
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.domain.model.CourseWithTopics
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure

interface AdminCourseRepository {
    suspend fun getAllCourses(page: Int = 1): Either<AdminFailure, List<Course>>

    suspend fun getCourseById(id: Int): Either<AdminFailure, CourseWithTopics>

    suspend fun createCourse(courseRequest: CreateCourseReq): Either<AdminFailure, Course>

    suspend fun updateCourse(id: Int, courseRequest: UpdateCourseReq): Either<AdminFailure, Course>

    suspend fun deleteCourse(id: Int): Either<AdminFailure, Unit>
}