package com.example.capyvocab_fe.admin.topic.data.remote.model

import com.example.capyvocab_fe.admin.word.domain.model.Word

data class TopicWordsResponse(
    val words: List<Word>,
    val total: Int = 0,
    val currentPage: Int = 1,
    val totalPages: Int = 0
)