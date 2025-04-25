package com.example.capyvocab_fe.admin.word.presentation

import com.example.capyvocab_fe.admin.word.domain.model.Word

data class WordListState(
    val words: List<Word> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val currentPage: Int = 1,
    val isEndReached: Boolean = false
)