package com.example.capyvocab_fe.admin.course.data.remote

import com.example.capyvocab_fe.admin.course.data.remote.model.CourseListResponse
import com.example.capyvocab_fe.admin.course.data.remote.model.CourseTopicsResponse
import com.example.capyvocab_fe.admin.course.data.remote.model.CreateCourseRequest
import com.example.capyvocab_fe.admin.course.data.remote.model.UpdateCourseRequest
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.core.network.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminCourseApi {
    @GET("/courses")
    suspend fun getAllCourses(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("sort") sort: String = "-id,+title"
    ): CourseListResponse

    @GET("/courses/{id}/topics")
    suspend fun getCourseTopics(
        @Path("id") id: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
    ): ApiResponse<CourseTopicsResponse>

    @POST("/courses")
    suspend fun createCourse(@Body courseRequest: CreateCourseRequest): ApiResponse<Course>

    @PATCH("/courses/{id}")
    suspend fun updateCourse(
        @Path("id") id: Int,
        @Body courseRequest: UpdateCourseRequest
    ): ApiResponse<Course>

    @DELETE("/courses/{id}")
    suspend fun deleteCourse(@Path("id") id: Int): ApiResponse<Any>

    @GET("/courses/{id}")
    suspend fun getCourseById(@Path("id") id: Int): ApiResponse<Course>
}