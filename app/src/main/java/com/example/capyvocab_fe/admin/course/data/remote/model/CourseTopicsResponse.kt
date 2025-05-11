package com.example.capyvocab_fe.admin.course.data.remote.model

import com.example.capyvocab_fe.admin.topic.domain.model.Topic

data class CourseTopicsResponse (
    val topics: List<Topic>,
    val total: Int = 0,
    val currentPage: Int = 1,
    val totalPages: Int = 0
)