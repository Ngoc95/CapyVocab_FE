package com.example.capyvocab_fe.admin.topic.data.remote

import com.example.capyvocab_fe.admin.topic.data.remote.model.CreateTopicReq
import com.example.capyvocab_fe.admin.topic.data.remote.model.UpdateTopicReq
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.user.data.remote.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AdminTopicApi {
    @POST("/topics")
    suspend fun createTopic(@Body topicRequest: CreateTopicReq): ApiResponse<Topic>
    @PATCH("/topics/{id}")
    suspend fun updateTopic(@Path("id") id: Int, @Body topicRequest: UpdateTopicReq): ApiResponse<Topic>
    @GET("/topics/{id}")
    suspend fun getTopicById(@Path("id") id: Int): ApiResponse<Topic>
    @DELETE("/topics/{id}")
    suspend fun deleteTopic(@Path("id") id: Int): ApiResponse<Any>

}
