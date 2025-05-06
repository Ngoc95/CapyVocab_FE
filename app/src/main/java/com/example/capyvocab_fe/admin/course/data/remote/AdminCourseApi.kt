package com.example.capyvocab_fe.admin.course.data.remote

import com.example.capyvocab_fe.admin.course.data.remote.model.CourseListResponse
import com.example.capyvocab_fe.admin.course.data.remote.model.CreateCourseReq
import com.example.capyvocab_fe.admin.course.data.remote.model.UpdateCourseReq
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.domain.model.CourseWithTopics
import com.example.capyvocab_fe.admin.user.data.remote.model.ApiResponse
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
        @Query(value = "sort", encoded = true) sort: String = "-id,+title"
    ): CourseListResponse

    @GET("/courses/{id}")
    suspend fun getCourseById(@Path("id") id: Int): ApiResponse<CourseWithTopics>

    @POST("/courses")
    suspend fun createCourse(@Body courseRequest: CreateCourseReq): ApiResponse<Course>

    @PATCH("/courses/{id}")
    suspend fun updateCourse(
        @Path("id") id: Int,
        @Body courseRequest: UpdateCourseReq
    ): ApiResponse<Course>

    @DELETE("/courses/{id}")
    suspend fun deleteCourse(@Path("id") id: Int): ApiResponse<Any>
}