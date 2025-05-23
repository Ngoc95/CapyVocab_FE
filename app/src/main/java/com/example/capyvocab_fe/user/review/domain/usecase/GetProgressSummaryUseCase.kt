package com.example.capyvocab_fe.user.review.domain.usecase

import com.example.capyvocab_fe.user.review.data.remote.model.ProgressSummaryResponse
import com.example.capyvocab_fe.user.review.domain.repository.UserReviewRepository
import javax.inject.Inject

class GetProgressSummaryUseCase @Inject constructor(
    private val repository: UserReviewRepository
) {
    suspend operator fun invoke(): ProgressSummaryResponse {
        return repository.getProgressSummary()
    }
}
