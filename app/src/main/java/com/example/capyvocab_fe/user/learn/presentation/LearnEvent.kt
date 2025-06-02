package com.example.capyvocab_fe.user.learn.presentation

import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic

sealed class LearnEvent {
    object LoadCourses : LearnEvent()
    object LoadMoreCourses : LearnEvent()
    data class GetCourseById(val courseId: Int): LearnEvent()

    data class LoadTopics(val course: Course) : LearnEvent()
    data class LoadMoreTopics(val course: Course) : LearnEvent()
    data class GetTopicById(val topicId: Int) : LearnEvent()
    object ClearTopics : LearnEvent()

    data class LoadWords(val topic: Topic) : LearnEvent()
    object FlipCard : LearnEvent()
    object ContinueToTyping : LearnEvent()
    data class SubmitAnswer(val answer: String) : LearnEvent()
    object ContinueAfterResult : LearnEvent()
    object AlreadyKnowWord : LearnEvent()
    object ClearWords : LearnEvent()
    object DismissCompletionDialog : LearnEvent()

    object ClearError : LearnEvent()
}