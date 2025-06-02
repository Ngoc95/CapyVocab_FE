package com.example.capyvocab_fe.user.review.domain.usecase

import com.example.capyvocab_fe.user.review.data.remote.model.WordProgressUpdate
import com.example.capyvocab_fe.user.review.domain.repository.UserReviewRepository
import javax.inject.Inject

class UpdateWordProgressUseCase @Inject constructor(
    private val repository: UserReviewRepository
) {
    suspend operator fun invoke(progressList: List<WordProgressUpdate>): Int {
        return repository.updateWordProgress(progressList)
    }
}
