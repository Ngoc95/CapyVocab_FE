package com.example.capyvocab_fe.admin.word.data.remote.model

import com.example.capyvocab_fe.admin.word.domain.model.Word

data class WordListResponse(
    val words: List<Word>,
    val total: Int,
    val currentPage: Int,
    val totalPages: Int
)