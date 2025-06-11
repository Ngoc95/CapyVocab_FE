package com.example.capyvocab_fe.user.review.domain.usecase

import com.example.capyvocab_fe.user.review.domain.model.WordReviewResult
import com.example.capyvocab_fe.user.review.domain.repository.UserReviewRepository
import javax.inject.Inject

class GetReviewWordsUseCase @Inject constructor(
    private val repository: UserReviewRepository
) {
    suspend operator fun invoke(): WordReviewResult {
        return repository.getReviewWords()
    }
}
