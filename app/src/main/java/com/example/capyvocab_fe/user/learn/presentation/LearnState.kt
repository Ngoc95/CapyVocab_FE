package com.example.capyvocab_fe.user.learn.presentation

import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic

data class LearnState(
    val isLoading: Boolean = false,
    val courses: List<Course> = emptyList(),
    val errorMessage: String = "",
    val currentCoursePage: Int = 1,
    val isEndReachedCourse: Boolean = false,
    val selectedCourse: Course? = null,
    val topics: List<Topic> = emptyList(),
    val selectedTopic: Topic? = null,
    val currentTopicPage: Int = 1,
    val isEndReachedTopic: Boolean = false,
)
