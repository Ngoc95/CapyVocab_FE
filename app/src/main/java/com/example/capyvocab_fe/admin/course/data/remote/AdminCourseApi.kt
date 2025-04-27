package com.example.capyvocab_fe.admin.course.data.remote

import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.user.data.remote.model.ApiResponse
import retrofit2.http.GET

interface AdminCourseApi {
    @GET("/admin/courses")
    suspend fun getAllCourses(): ApiResponse<List<Course>>
}