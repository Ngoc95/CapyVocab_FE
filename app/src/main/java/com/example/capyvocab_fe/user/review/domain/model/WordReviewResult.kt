package com.example.capyvocab_fe.user.review.domain.model

import com.example.capyvocab_fe.admin.word.domain.model.Word

data class WordReviewResult(
    val words: List<Word>,
    val total: Int
)
