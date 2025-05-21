package com.example.capyvocab_fe.user.test.domain.model

import com.example.capyvocab_fe.auth.domain.model.User

data class Folder(
    val id: Int,
    val name: String,
    val code: String,
    val createdBy: User?,
    val voteCount: Int,
    val commentCount: Int,
    val isAlreadyVote: Boolean,
    val quizzes: List<Quiz>?,
    val flashCards: List<FlashCard>?,
    val comments: List<Comment>?
)
