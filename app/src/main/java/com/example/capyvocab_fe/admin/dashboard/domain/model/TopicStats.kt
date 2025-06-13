package com.example.capyvocab_fe.admin.dashboard.domain.model

data class TopicStats(
    val totalTopics: Int,
    val progressStats: TopicProgressStats,
    val popularTopic: List<TopTopic>
)

data class TopicProgressStats(
    val completed: Double,
    val notCompleted: Double
)

data class TopTopic(
    val topic: Int,
    val completeCount: Int
)

data class TopTopicDetail(
    val id: Int,
    val title: String,
    val wordCount: Int,
    val completeCount: Int
)
