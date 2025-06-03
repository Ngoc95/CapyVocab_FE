package com.example.capyvocab_fe.user.test.domain.model

data class Quiz(
    val id: Int,
    val title: String,
    val question: List<Question>
)

data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswers: List<String>,
    val explanation: String? = null,
    val image: String? = null,
    val time: String? = null,
    val type: String = "SINGLE_CHOICE" // SINGLE_CHOICE, MULTIPLE_CHOICE, FILL_IN_BLANK
)
