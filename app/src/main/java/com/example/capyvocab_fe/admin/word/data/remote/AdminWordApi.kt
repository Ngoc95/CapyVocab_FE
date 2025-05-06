package com.example.capyvocab_fe.admin.word.data.remote

import com.example.capyvocab_fe.admin.word.data.remote.model.CreateWordRequest
import com.example.capyvocab_fe.admin.word.data.remote.model.DeleteResponse
import com.example.capyvocab_fe.admin.word.data.remote.model.RestoreResponse
import com.example.capyvocab_fe.admin.word.data.remote.model.UpdateWordRequest
import com.example.capyvocab_fe.admin.word.data.remote.model.WordListResponse
import com.example.capyvocab_fe.admin.word.domain.model.Word
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminWordApi {

    @GET("/words")
    suspend fun getAllWords(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
    ): WordListResponse

    @GET("/words/{id}")
    suspend fun getWordById(
        @Path("id") id: Int
    ): Word

    @POST("/words")
    suspend fun createWords(
        @Body words: List<CreateWordRequest>
    ): List<Word>

    @PATCH("/words/{id}")
    suspend fun updateWord(
        @Path("id") id: Int,
        @Body word: UpdateWordRequest
    ): Word

    @DELETE("/words/{id}")
    suspend fun deleteWordById(
        @Path("id") id: Int
    ): DeleteResponse

    @PATCH("/words/{id}/restore")
    suspend fun restoreWord(
        @Path("id") id: Int
    ): RestoreResponse
}