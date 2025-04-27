package com.example.capyvocab_fe.admin.topic.domain.model

data class Topic(
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnail: String?,
    val type: Int,
    val totalWords: Int,
)
