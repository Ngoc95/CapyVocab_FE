package com.example.capyvocab_fe.admin.course.domain.model

data class Course(
    val id: Int,
    val title: String,
    val level: Int,
    val target: String?,
    val description: String?,
    val totalTopics: Int,
)
