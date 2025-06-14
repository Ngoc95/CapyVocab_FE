package com.example.capyvocab_fe.user.learn.presentation

import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.word.domain.model.Word

data class LearnState(
    val isLoading: Boolean = false,
    val courses: List<Course> = emptyList(),
    val errorMessage: String = "",
    val currentCoursePage: Int = 1,
    val isEndReachedCourse: Boolean = false,
    val selectedCourse: Course? = null,

    //topic
    val topics: List<Topic> = emptyList(),
    val selectedTopic: Topic? = null,
    val currentTopicPage: Int = 1,
    val isEndReachedTopic: Boolean = false,

    //learn
    val words: List<Word> = emptyList(),
    val currentIndex: Int = 0,
    val correctCount: Int = 0,
    val isFlipped: Boolean = false,
    val isTyping: Boolean = false,
    val answerResult: Boolean? = null,
    val showResult: Boolean = false,
    val isLearning: Boolean = true,
    val isComplete: Boolean = false,
    val isMarkingComplete: Boolean = false,
    val showCompletionDialog: Boolean = false,

    //search
    val searchQuery: String = ""

) {
    val currentWord: Word?
        get() = words.getOrNull(currentIndex)
}
