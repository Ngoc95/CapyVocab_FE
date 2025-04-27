package com.example.capyvocab_fe.admin.topic.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.topic.domain.repository.AdminTopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
            is TopicEvent.LoadTopics -> loadTopics(event.courseId)
//            is TopicEvent.CreateTopic -> createTopic(event.courseId, event.topic)
//            is TopicEvent.UpdateTopic -> updateTopic(event.topic)
//            is TopicEvent.DeleteTopic -> deleteTopic(event.topicId)
        }
    }

    fun loadTopics(courseId: Int) {
        viewModelScope.launch {
            topicRepository.getTopicsByCourse(courseId)
                .onRight { topics ->
                    _state.value = _state.value.copy(
                        topics = topics,
                        isLoading = false
                    )
                }
                .onLeft { failure ->
                    _state.value = _state.value.copy(
                        error = failure.message ?: "Unknown error",
                        isLoading = false
                    )
                }
        }
    }


}