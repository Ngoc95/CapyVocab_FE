package com.example.capyvocab_fe.admin.topic.domain.model

data class Topic(
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnail: String?,
    val type: String,
    val deletedAt: String?,
    val createdAt: String,
    val updatedAt: String,
    val displayOrder: Int? = null
)
