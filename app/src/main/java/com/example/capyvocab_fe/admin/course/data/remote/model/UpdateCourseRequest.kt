package com.example.capyvocab_fe.admin.course.data.remote.model

data class UpdateCourseRequest(
    val title: String,
    val description: String? = null,
    val target: String? = null,
    val level: String,
    val topics: List<TopicRequest> = emptyList()
)