package com.example.capyvocab_fe.user.review.data.remote

import com.example.capyvocab_fe.core.network.ApiResponse
import com.example.capyvocab_fe.user.review.data.remote.model.ProgressRequest
import com.example.capyvocab_fe.user.review.data.remote.model.ProgressSummaryResponse
import com.example.capyvocab_fe.user.review.data.remote.model.WordProgressUpdateResponse
import com.example.capyvocab_fe.user.review.data.remote.model.WordReviewResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserReviewApi {
    @GET("progress/word-review")
    suspend fun getReviewWords(): ApiResponse<WordReviewResponse>

    @PUT("progress/word")
    suspend fun updateProgress(@Body request: ProgressRequest): ApiResponse<WordProgressUpdateResponse>

    @GET("progress/summary")
    suspend fun getProgressSummary(): ApiResponse<ProgressSummaryResponse>
}