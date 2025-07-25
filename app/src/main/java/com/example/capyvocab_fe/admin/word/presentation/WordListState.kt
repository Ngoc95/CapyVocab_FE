package com.example.capyvocab_fe.admin.word.presentation

import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.word.domain.model.Word

data class WordListState(
    val words: List<Word> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = "",
    val currentPage: Int = 1,
    val isEndReached: Boolean = false,
    val selectedWords: Set<Int> = emptySet(),
    val isMultiSelecting: Boolean = false,
    val isSelectAll: Boolean = false,
    val currentTopic: Topic? = null,
    val searchQuery: String = ""
)