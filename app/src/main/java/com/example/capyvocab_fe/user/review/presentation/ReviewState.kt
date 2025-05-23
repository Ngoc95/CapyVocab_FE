package com.example.capyvocab_fe.user.review.presentation

import com.example.capyvocab_fe.admin.word.domain.model.Word
import java.util.UUID

data class ReviewState(
    //review
    val words: List<Word> = emptyList(),
    val currentIndex: Int = 0,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val error: String? = null,
    val completed: Boolean = false,
    val showFeedback: Boolean = false,
    val questionSessionId: String = UUID.randomUUID().toString(),
    val isCorrectAnswer: Boolean = false,
    val isTrueMeaning: Boolean? = null,
    val selectedAnswer: String? = null,
    val selectedAnswerBoolean: Boolean? = null,
    val currentCorrectAnswer: String? = null,
    val totalWordsToReview: Int = 0,
    val preparedCount: Int = 0,
    val hasStarted: Boolean = false,
    val wrongCountMap: Map<String, Int> = emptyMap(), // wordId -> count
    val totalUpdated: Int = 0,
    val correctCount: Int = 0, // đếm số từ trả lời đúng

    //chart
    val progressChart: List<ProgressChartItem> = emptyList(),
    val totalLearnWord: Int = 0
)

data class ProgressChartItem(val label: String, val count: Int)