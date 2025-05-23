package com.example.capyvocab_fe.user.review.domain.repository

import com.example.capyvocab_fe.user.review.data.remote.model.ProgressSummaryResponse
import com.example.capyvocab_fe.user.review.data.remote.model.WordProgressUpdate
import com.example.capyvocab_fe.user.review.domain.model.WordReviewResult

interface UserReviewRepository {
    suspend fun getReviewWords(): WordReviewResult
    suspend fun updateWordProgress(progressList: List<WordProgressUpdate>): Int
    suspend fun getProgressSummary(): ProgressSummaryResponse
}
