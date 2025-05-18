package com.example.capyvocab_fe.admin.word.data.remote.model

data class CreateWordRequest(
    val words: List<CreateWordBody>
)

data class CreateWordBody(
    val content: String,
    val pronunciation: String,
    val position: String,
    val meaning: String,
    val audio: String,
    val image: String,
    val example: String,
    val translateExample: String,
    val topicIds: List<Int> = emptyList()
)

