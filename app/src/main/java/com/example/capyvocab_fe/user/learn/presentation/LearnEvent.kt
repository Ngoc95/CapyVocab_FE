package com.example.capyvocab_fe.user.learn.presentation

import com.example.capyvocab_fe.admin.course.domain.model.Course

sealed class LearnEvent {
    object LoadCourses : LearnEvent()
    object LoadMoreCourses : LearnEvent()
    data class GetCourseById(val courseId: Int): LearnEvent()
    data class LoadTopics(val course: Course) : LearnEvent()
    data class LoadMoreTopics(val course: Course) : LearnEvent()
    data class GetTopicById(val topicId: Int) : LearnEvent()
}