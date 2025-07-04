package com.example.capyvocab_fe.admin.topic.presentation

import android.net.Uri
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic

sealed class TopicEvent {
    object LoadAllTopics : TopicEvent()
    object LoadMoreAllTopics : TopicEvent()
    data class LoadTopics(val course: Course) : TopicEvent()
    data class LoadMoreTopics(val course: Course) : TopicEvent()

    data class UpdateTopic(
        val topic: Topic,
        val thumbnailUri: Uri? = null,
    ) : TopicEvent()

    data class CreateTopic(
        val courseId: Int,
        val topic: Topic,
        val thumbnailUri: Uri? = null
    ) : TopicEvent()

    data class DeleteTopic(val topicId: Int) : TopicEvent()
    data class GetTopicById(val topicId: Int) : TopicEvent()

    data class OnTopicLongPress(val topicId: Int) : TopicEvent()
    data class OnTopicSelectToggle(val topicId: Int) : TopicEvent()
    object OnSelectAllToggle : TopicEvent()
    object OnDeleteSelectedTopics : TopicEvent()
    object CancelMultiSelect : TopicEvent()
    data class OnSearchQueryChange(val query: String) : TopicEvent()
    object OnSearch: TopicEvent()
}
