package com.example.capyvocab_fe.admin.topic.presentation

import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic

data class TopicListState(
    val isLoading: Boolean = true,
    val topics: List<Topic> = emptyList(),
    val errorMessage: String = "",
    val successMessage: String = "",
    val currentPage: Int = 1,
    val isEndReached: Boolean = false,
    val selectedTopics: Set<Int> = emptySet(),
    val isMultiSelecting: Boolean = false,
    val isSelectAll: Boolean = false,
    val selectedTopic: Topic? = null,
    val currentCourse: Course? = null,
    val searchQuery: String = ""
)
