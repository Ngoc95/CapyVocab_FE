package com.example.capyvocab_fe.user.review.presentation

sealed class ReviewEvent {
    object LoadWords : ReviewEvent()
    object StartReview : ReviewEvent()
    data class SubmitAnswer(val answer: String) : ReviewEvent()
    object Continue : ReviewEvent()
    object Complete : ReviewEvent()
    object LoadProgressSummary : ReviewEvent()
}
