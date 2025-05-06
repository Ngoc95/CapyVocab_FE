package com.example.capyvocab_fe.admin.course.presentation

import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.domain.model.CourseWithTopics

data class CourseListState(
    val isLoading: Boolean = false,
    val courses: List<Course> = emptyList(),
    val errorMessage: String = "",
    val currentPage: Int = 1,
    val isEndReached: Boolean = false,
    val selectedCourses: Set<Int> = emptySet(),
    val isMultiSelecting: Boolean = false,
    val isSelectAll: Boolean = false,
    val selectedCourse: CourseWithTopics? = null
)
