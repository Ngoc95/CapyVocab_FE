package com.example.capyvocab_fe.user.test.presentation.viewmodel

import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.user.test.domain.model.Comment
import com.example.capyvocab_fe.user.test.domain.model.Folder
import com.example.capyvocab_fe.user.test.domain.model.Quiz

data class ExerciseState(
    // Loading states
    val isLoading: Boolean = false,

    // Error handling
    val error: AppFailure? = null,
    val successMessage: String? = null,

    // Data states
    val folders: List<Folder> = emptyList(),
    val currentFolder: Folder? = null,
    val comments: List<Comment> = emptyList(),
    val childComments: Map<Int, List<Comment>> = emptyMap(),

    // Pagination
    val currentPage: Int = 1,
    val isEndReached: Boolean = false,

    // Quiz state
    val currentQuiz: Quiz? = null,
    val selectedAnswers: Map<Int, List<String>> = emptyMap(), // Question index to selected answers

    // Navigation state
    val currentTab: Int = 0, // 0: DoTest, 1: EnterCode, 2: CreatedTests, 3: CreateTest

    val currentUser: User? = null
)