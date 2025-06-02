package com.example.capyvocab_fe.admin.course.data.remote.model

import com.example.capyvocab_fe.admin.course.domain.model.Course

data class CourseListResponse(
    val metaData: MetaData
)

data class MetaData(
    val courses: List<Course> = emptyList(),
    val total: Int = 0,
    val currentPage: Int = 1,
    val totalPages: Int = 0
)