package com.example.capyvocab_fe.admin.word.data.remote

import com.example.capyvocab_fe.core.network.ApiResponse
import com.example.capyvocab_fe.admin.user.data.remote.model.ImageUploadResponse
import com.example.capyvocab_fe.admin.word.data.remote.model.CreateWordRequest
import com.example.capyvocab_fe.admin.word.data.remote.model.UpdateWordRequest
import com.example.capyvocab_fe.admin.word.data.remote.model.WordListResponse
import com.example.capyvocab_fe.admin.word.domain.model.Word
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

interface AdminWordApi {

    @GET("/words")
    suspend fun getAllWords(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
    ): ApiResponse<WordListResponse>

    @GET("/words/{id}")
    suspend fun getWordById(
        @Path("id") id: Int
    ): ApiResponse<Word>

    @POST("/words")
    suspend fun createWords(
        @Body createWordRequest: CreateWordRequest
    ): ApiResponse<List<Word>>

    @PATCH("/words/{id}")
    suspend fun updateWord(
        @Path("id") id: Int,
        @Body updateWordRequest: UpdateWordRequest
    ): ApiResponse<Word>

    @DELETE("/words/{id}")
    suspend fun deleteWordById(
        @Path("id") id: Int
    ): ApiResponse<Any>

    @Multipart
    @POST("/upload/images")
    suspend fun uploadImage(
        @Part("type") type: RequestBody,
        @Part images: MultipartBody.Part
    ): ImageUploadResponse
}