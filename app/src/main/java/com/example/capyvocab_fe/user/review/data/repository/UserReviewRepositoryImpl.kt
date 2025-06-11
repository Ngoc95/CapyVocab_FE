package com.example.capyvocab_fe.user.review.data.repository

import com.example.capyvocab_fe.user.review.data.remote.UserReviewApi
import com.example.capyvocab_fe.user.review.data.remote.model.ProgressRequest
import com.example.capyvocab_fe.user.review.data.remote.model.ProgressSummaryResponse
import com.example.capyvocab_fe.user.review.data.remote.model.WordProgressUpdate
import com.example.capyvocab_fe.user.review.domain.model.WordReviewResult
import com.example.capyvocab_fe.user.review.domain.repository.UserReviewRepository
import javax.inject.Inject

class UserReviewRepositoryImpl @Inject constructor(
    private val api: UserReviewApi
) : UserReviewRepository {
    override suspend fun getReviewWords(): WordReviewResult {
        val response = api.getReviewWords()
        val words = response.metaData.words.map { it.word }
        val total = response.metaData.total
        return WordReviewResult(words, total)
    }

    override suspend fun updateWordProgress(progressList: List<WordProgressUpdate>): Int {
        val response = api.updateProgress(ProgressRequest(progressList))
        return response.metaData?.summary?.totalUpdated ?: 0
    }

    override suspend fun getProgressSummary(): ProgressSummaryResponse {
        return api.getProgressSummary().metaData
    }
}
