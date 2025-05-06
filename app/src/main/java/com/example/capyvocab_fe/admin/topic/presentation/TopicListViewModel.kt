package com.example.capyvocab_fe.admin.topic.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.course.domain.model.CourseWithTopics
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.topic.domain.repository.AdminTopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicListViewModel @Inject constructor(
    private val topicRepository: AdminTopicRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TopicListState())
    val state: StateFlow<TopicListState> = _state

    fun onEvent(event: TopicEvent) {
        when (event) {
            is TopicEvent.LoadTopics -> loadTopics(event.course)
            is TopicEvent.DeleteTopic -> deleteTopic(event.topicId)
            is TopicEvent.SaveTopic -> saveTopic(event.topic)
            is TopicEvent.GetTopicById -> getTopicById(event.topicId)
        }
    }

    private fun getTopicById(topicId: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, errorMessage = "")
            }
            topicRepository.getTopicById(topicId)
                .onRight { topic ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedTopic = topic
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "failed to load topic"
                        )
                    }
                }
        }
    }

    private fun loadTopics(course: CourseWithTopics) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    topics = course.topics
                )
            }
        }
    }

    private fun saveTopic(topic: Topic) {

    }

    private fun deleteTopic(topicId: Int) {

    }
}