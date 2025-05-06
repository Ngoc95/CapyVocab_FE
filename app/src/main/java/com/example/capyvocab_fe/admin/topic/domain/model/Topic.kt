package com.example.capyvocab_fe.admin.topic.domain.model

import com.example.capyvocab_fe.admin.word.domain.model.Word

data class Topic(
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnail: String?,
    val type: String,
    val words: List<Word>,
    val deletedAt: String?,
    val createdAt: String,
    val updatedAt: String
)
