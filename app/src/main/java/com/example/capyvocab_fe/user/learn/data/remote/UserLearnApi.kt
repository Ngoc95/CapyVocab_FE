package com.example.capyvocab_fe.user.learn.data.remote

import com.example.capyvocab_fe.admin.course.data.remote.model.CourseListResponse
import com.example.capyvocab_fe.admin.course.data.remote.model.CourseTopicsResponse
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.data.remote.model.TopicWordsResponse
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.core.network.ApiResponse
import com.example.capyvocab_fe.user.learn.data.remote.model.CompleteTopicRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserLearnApi {
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

    @GET("/courses/{id}")
    suspend fun getCourseById(@Path("id") id: Int): ApiResponse<Course>

    @GET("/topics/{id}/words")
    suspend fun getTopicWords(
        @Path("id") id: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): ApiResponse<TopicWordsResponse>

    @GET("/topics/{id}")
    suspend fun getTopicById(@Path("id") id: Int): ApiResponse<Topic>

    @POST("/progress/complete-topic")
    suspend fun markTopicComplete(@Body body: CompleteTopicRequest): ApiResponse<Unit>
}