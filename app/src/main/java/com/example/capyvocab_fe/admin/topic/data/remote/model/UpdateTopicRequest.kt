package com.example.capyvocab_fe.admin.topic.data.remote.model

data class UpdateTopicRequest (
    val title: String,
    val description: String?,
    val thumbnail: String?,
    val type: String
)