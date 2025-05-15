package com.example.capyvocab_fe.admin.topic.domain.model

import com.example.capyvocab_fe.admin.word.domain.model.Word

data class Topic(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: String,
    val type: String,
    val displayOrder: Int? = null,
    val words: List<Word> = emptyList()
)
