package com.example.capyvocab_fe.admin.topic.data.remote.model

data class UpdateTopicReq (
    val topics: List<UpdateTopicBody>
)

data class UpdateTopicBody (
    val title: String,
    val description: String,
    val thumbnail: String,
    val type: String
)