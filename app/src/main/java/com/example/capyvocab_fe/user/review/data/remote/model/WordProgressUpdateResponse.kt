package com.example.capyvocab_fe.user.review.data.remote.model

data class WordProgressUpdateResponse(
    val updatedWords: List<UpdatedWord>,
    val summary: WordProgressSummary
)
data class UpdatedWord(
    val wordId: Int,
    val masteryLevel: Int,
    val easeFactor: Int,
    val reviewCount: Int,
    val nextReviewDate: String
)
data class WordProgressSummary(
    val totalUpdated: Int
)