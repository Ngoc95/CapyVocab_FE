package com.example.capyvocab_fe.admin.topic.presentation

import com.example.capyvocab_fe.admin.topic.domain.model.Topic

data class TopicListState(
    val isLoading: Boolean = true,
    val topics: List<Topic> = emptyList(),
    val error: String? = null
)
