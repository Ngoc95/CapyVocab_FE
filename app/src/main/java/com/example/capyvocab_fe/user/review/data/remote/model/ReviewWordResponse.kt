package com.example.capyvocab_fe.user.review.data.remote.model

import com.example.capyvocab_fe.admin.word.domain.model.Word

data class WordReviewItemDto(
    val word: Word,
    val masteryLevel: Int,
    val reviewCount: Int,
    val nextReviewDate: String
)

data class WordReviewResponse(
    val words: List<WordReviewItemDto>,
    val total: Int,
    val currentPage: Int,
    val totalPages: Int
)