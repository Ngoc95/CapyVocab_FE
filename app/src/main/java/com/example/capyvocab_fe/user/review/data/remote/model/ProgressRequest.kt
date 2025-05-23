package com.example.capyvocab_fe.user.review.data.remote.model

data class ProgressRequest(
    val wordProgress: List<WordProgressUpdate>
)
data class WordProgressUpdate(
    val wordId: Int,
    val wrongCount: Int,
    val reviewedDate: String
)