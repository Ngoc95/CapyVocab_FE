package com.example.capyvocab_fe.admin.course.presentation

import com.example.capyvocab_fe.admin.course.domain.model.Course

data class CourseListState(
    val isLoading: Boolean = true,
    val courses: List<Course> = emptyList(),
    val error: String? = null
)
