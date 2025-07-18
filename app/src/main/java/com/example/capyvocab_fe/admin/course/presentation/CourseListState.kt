package com.example.capyvocab_fe.admin.course.presentation

import com.example.capyvocab_fe.admin.course.domain.model.Course

data class CourseListState(
    val isLoading: Boolean = false,
    val courses: List<Course> = emptyList(),
    val errorMessage: String = "",
    val successMessage: String = "",
    val currentPage: Int = 1,
    val isEndReached: Boolean = false,
    val selectedCourses: Set<Int> = emptySet(),
    val isMultiSelecting: Boolean = false,
    val isSelectAll: Boolean = false,
    val selectedCourse: Course? = null,
    val searchQuery: String = "",
    val selectedLevel: String? = null
)
