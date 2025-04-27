package com.example.capyvocab_fe.admin.topic.presentation

import com.example.capyvocab_fe.admin.topic.domain.model.Topic

sealed class TopicEvent {
    data class LoadTopics(val courseId: Int) : TopicEvent()
//    data class CreateTopic(val courseId: Int, val topic: Topic) : TopicEvent()
//    data class UpdateTopic(val topic: Topic) : TopicEvent()
//    data class DeleteTopic(val topicId: Int) : TopicEvent()
}
