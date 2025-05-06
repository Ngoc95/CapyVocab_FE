package com.example.capyvocab_fe.admin.course.data.remote.model

data class CreateCourseReq(
    val courses: List<CourseBody>
)

data class CourseBody (
    val title: String,
    val description: String? = null,
    val target: String? = null,
    val level: String,
    val topics: List<TopicRequest>? = null
)

data class TopicRequest(
    val id: Int,
    val displayOrder: Int
)