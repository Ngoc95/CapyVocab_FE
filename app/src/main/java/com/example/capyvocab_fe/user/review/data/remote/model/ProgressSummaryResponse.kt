package com.example.capyvocab_fe.user.review.data.remote.model

data class ProgressSummaryResponse(
    val statistics: List<StatisticItem>?, // nullable
    val totalLearnWord: Int?
)

data class StatisticItem(
    val level: String,
    val wordCount: Int
)
