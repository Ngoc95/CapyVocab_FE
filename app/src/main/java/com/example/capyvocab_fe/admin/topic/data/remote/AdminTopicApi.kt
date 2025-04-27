package com.example.capyvocab_fe.admin.topic.data.remote

import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.user.data.remote.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AdminTopicApi {
    @GET("/admin/courses/{courseId}/topics")
    suspend fun getTopicsByCourse(@Path("courseId") courseId: Int): ApiResponse<List<Topic>>
}
