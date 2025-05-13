package com.example.capyvocab_fe.admin.word.domain.model

data class Word(
    val id: Int,
    val content: String,
    val pronunciation: String,
    val position: String,
    val meaning: String,
    val rank: String,
    val audio: String,
    val image: String,
    val example: String,
    val translateExample: String,
)
