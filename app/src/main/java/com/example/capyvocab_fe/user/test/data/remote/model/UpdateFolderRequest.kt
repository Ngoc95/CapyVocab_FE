package com.example.capyvocab_fe.user.test.data.remote.model

data class UpdateFolderRequest(
    val name: String? = null,
    val price: Double? = null,
    val isPublic: Boolean? = null,
    val quizzes: List<QuizRequest>? = null,
    val flashCards: List<FlashCardRequest>? = null
)

data class QuizRequest(
    val id: Int? = null,
    val title: String,
    val question: List<QuestionRequest>
)

data class QuestionRequest(
    val question: String,
    val options: List<String>,
    val correctAnswers: List<String>,
    val explanation: String? = null,
    val image: String? = null,
    val time: String? = null,
    val type: String = "SINGLE_CHOICE" // SINGLE_CHOICE, MULTIPLE_CHOICE, FILL_IN_BLANK
)

data class FlashCardRequest(
    val frontContent: String,
    val frontImage: String? = null,
    val backContent: String,
    val backImage: String? = null
)
