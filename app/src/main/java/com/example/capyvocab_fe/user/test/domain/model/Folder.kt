package com.example.capyvocab_fe.user.test.domain.model

import com.example.capyvocab_fe.auth.domain.model.User

data class Folder(
    val id: Int,
    val name: String,
    val code: String,
    val price: Double,
    val isPublic: Boolean = false,
    val createdBy: User?,
    val voteCount: Int,
    val commentCount: Int,
    val isAlreadyVote: Boolean,
    val quizzes: List<Quiz>? = emptyList(),
    val flashCards: List<FlashCard>? = emptyList(),
    val comments: List<Comment>? = emptyList()
) {
    val isFree: Boolean get() = price == 0.0
}
