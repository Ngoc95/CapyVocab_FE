package com.example.capyvocab_fe.admin.topic.data.remote.model

data class CreateTopicRequest(
    val topics: List<CreateTopicBody>
)

data class CreateTopicBody(
    val title: String,
    val description: String?,
    val thumbnail: String?,
    val type: String,
    val wordIds: List<Int>? = null,
    val courseIds: List<Int>
)