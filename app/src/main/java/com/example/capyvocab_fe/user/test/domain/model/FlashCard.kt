package com.example.capyvocab_fe.user.test.domain.model

data class FlashCard(
    val id: Int,
    val frontContent: String,
    val frontImage: String?,
    val backContent: String,
    val backImage: String?
)
