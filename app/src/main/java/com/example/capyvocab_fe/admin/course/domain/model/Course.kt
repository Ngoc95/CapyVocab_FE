package com.example.capyvocab_fe.admin.course.domain.model


data class Course (
    val id: Int,
    val title: String,
    val level: String,
    val target: String,
    val description: String?,
    val courseTopics: List<CourseTopics>
)

data class CourseTopics (
    val id: Int,
    val displayOrder: Int,
    val deletedAt: String?,
    val createdAt: String,
    val updatedAt: String
)

data class CourseWithTopics(
    val id: Int,
    val title: String,
    val level: String,
    val target: String,
    val description: String?,
    val topics: List<TopicInCourse>
)

data class TopicInCourse(
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnail: String?,
    val type: String,
    val deletedAt: String?,
    val createdAt: String,
    val updatedAt: String,
    val displayOrder: Int
)