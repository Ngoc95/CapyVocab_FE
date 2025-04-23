package com.example.capyvocab_fe.admin.word.data.remote.model

data class CreateWordRequest(
    val content: String,
    val pronunciation: String,
    val position: String,
    val meaning: String,
    val audio: String,
    val image: String,
    val rank: String,
    val example: String,
    val translateExample: String
)

data class UpdateWordRequest(
    val content: String,
    val pronunciation: String,
    val position: String,
    val meaning: String,
    val audio: String,
    val image: String,
    val rank: String,
    val example: String,
    val translateExample: String,
    val topicIds: List<Int> = emptyList()
)
