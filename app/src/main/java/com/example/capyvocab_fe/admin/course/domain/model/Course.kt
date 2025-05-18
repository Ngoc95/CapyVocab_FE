package com.example.capyvocab_fe.admin.course.domain.model

import com.example.capyvocab_fe.admin.topic.domain.model.Topic


data class Course(
    val id: Int,
    val title: String,
    val level: String,
    val target: String,
    val description: String?,
    val courseTopics: List<CourseTopics>,
    val topics: List<Topic> = emptyList()
)

data class CourseTopics(
    val id: Int,
    val displayOrder: Int,
    val deletedAt: String?,
    val createdAt: String,
    val updatedAt: String
)