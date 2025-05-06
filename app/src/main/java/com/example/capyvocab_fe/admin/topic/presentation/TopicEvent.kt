package com.example.capyvocab_fe.admin.topic.presentation

import com.example.capyvocab_fe.admin.course.domain.model.CourseWithTopics
import com.example.capyvocab_fe.admin.topic.domain.model.Topic

sealed class TopicEvent {
    data class LoadTopics(val course: CourseWithTopics) : TopicEvent()
    data class SaveTopic(val topic: Topic): TopicEvent()
    data class DeleteTopic(val topicId: Int) : TopicEvent()
    data class GetTopicById(val topicId: Int): TopicEvent()
}
