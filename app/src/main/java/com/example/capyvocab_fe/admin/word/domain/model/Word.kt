package com.example.capyvocab_fe.admin.word.domain.model

data class Word(
    val id: Int,
    val content: String,
    val pronunciation: String,
    val position: String,
    val meaning: String,
    val audio: String,
    val image: String,
    val example: String,
    val translateExample: String,
    val deletedAt: String?,
    val createdAt: String,
    val updatedAt: String
)
