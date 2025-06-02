package com.example.capyvocab_fe.admin.topic.data.remote

import com.example.capyvocab_fe.admin.topic.data.remote.model.CreateTopicRequest
import com.example.capyvocab_fe.admin.topic.data.remote.model.TopicListResponse
import com.example.capyvocab_fe.admin.topic.data.remote.model.TopicWordsResponse
import com.example.capyvocab_fe.admin.topic.data.remote.model.UpdateTopicRequest
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.user.data.remote.model.ImageUploadResponse
import com.example.capyvocab_fe.core.network.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminTopicApi {
    @GET("/topics")
    suspend fun getAllTopic(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("title") title: String? = null
    ): ApiResponse<TopicListResponse>

    @POST("/topics")
    suspend fun createTopic(@Body topicRequest: CreateTopicRequest): ApiResponse<List<Topic>>

    @PATCH("/topics/{id}")
    suspend fun updateTopic(
        @Path("id") id: Int,
        @Body topicRequest: UpdateTopicRequest
    ): ApiResponse<Topic>

    @GET("/topics/{id}/words")
    suspend fun getTopicWords(
        @Path("id") id: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
    ): ApiResponse<TopicWordsResponse>

    @DELETE("/topics/{id}")
    suspend fun deleteTopic(@Path("id") id: Int): ApiResponse<Any>

    @GET("/topics/{id}")
    suspend fun getTopicById(@Path("id") id: Int): ApiResponse<Topic>

    @Multipart
    @POST("/upload/images")
    suspend fun uploadThumbnailImage(
        @Part("type") type: RequestBody,
        @Part images: MultipartBody.Part
    ): ImageUploadResponse
}
