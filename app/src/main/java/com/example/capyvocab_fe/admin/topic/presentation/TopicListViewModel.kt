package com.example.capyvocab_fe.admin.topic.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.domain.repository.AdminCourseRepository
import com.example.capyvocab_fe.admin.topic.data.remote.model.CreateTopicBody
import com.example.capyvocab_fe.admin.topic.data.remote.model.CreateTopicRequest
import com.example.capyvocab_fe.admin.topic.data.remote.model.UpdateTopicRequest
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
    private val topicRepository: AdminTopicRepository,
    private val courseRepository: AdminCourseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TopicListState())
    val state: StateFlow<TopicListState> = _state

    fun onEvent(event: TopicEvent) {
        when (event) {
            is TopicEvent.LoadTopics -> loadTopics(event.course)
            is TopicEvent.LoadMoreTopics -> loadTopics(event.course, loadMore = true)
            is TopicEvent.DeleteTopic -> deleteTopic(event.topicId)
            is TopicEvent.UpdateTopic -> updateTopic(event.topic)
            is TopicEvent.CreateTopic -> createTopic(event.course, event.topic)
            is TopicEvent.GetTopicById -> getTopicById(event.topicId)
            is TopicEvent.CancelMultiSelect -> cancelMultiSelect()
            is TopicEvent.OnDeleteSelectedTopics -> deleteSelectedTopics()
            is TopicEvent.OnSelectAllToggle -> selectAll()
            is TopicEvent.OnTopicLongPress -> startMultiSelect(event.topicId)
            is TopicEvent.OnTopicSelectToggle -> toggleTopicSelection(event.topicId)
        }
    }

    private fun createTopic(
        course: Course,
        topic: Topic
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            val createTopicRequest = CreateTopicRequest(
                topics = listOf(
                    CreateTopicBody(
                        title = topic.title,
                        description = topic.description,
                        thumbnail = topic.thumbnail,
                        type = topic.type,
                        courseIds = listOf(course.id)
                    )
                )
            )

            topicRepository.createTopic(createTopicRequest)
                .onRight { newTopic ->
                    _state.update { currentState ->
                        val updatedTopics = currentState.topics + newTopic
                        currentState.copy(
                            isLoading = false,
                            errorMessage = "",
                            topics = updatedTopics
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "failed to create topic"
                        )
                    }
                }
        }
    }

    private fun getTopicById(topicId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

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
                            errorMessage = failure.message ?: "Đã xảy ra lỗi"
                        )
                    }
                }
        }
    }

    private fun loadTopics(course: Course, loadMore: Boolean = false) {
        viewModelScope.launch {
            val nextPage = if (loadMore) state.value.currentPage + 1 else 1
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            courseRepository.getCourseTopics(course.id, nextPage)
                .onRight { newTopics ->
                    _state.update {
                        val allTopics = if (loadMore) it.topics + newTopics else newTopics
                        it.copy(
                            isLoading = false,
                            topics = allTopics,
                            currentPage = nextPage,
                            isEndReached = newTopics.isEmpty()
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "failed to load topics"
                        )
                    }
                }
        }
    }

    private fun updateTopic(topic: Topic) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            val updateTopicRequest = UpdateTopicRequest(
                title = topic.title,
                description = topic.description,
                thumbnail = topic.thumbnail,
                type = topic.type
            )

            topicRepository.updateTopic(topic.id, updateTopicRequest)
                .onRight { updatedTopic ->
                    _state.update { currentState ->
                        val updatedTopics = currentState.topics.map {
                            if (it.id == updatedTopic.id) updatedTopic else it
                        }
                        currentState.copy(
                            isLoading = false,
                            errorMessage = "",
                            topics = updatedTopics
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "failed to update topic"
                        )
                    }
                }
        }
    }

    private fun deleteTopic(topicId: Int) {
//        viewModelScope.launch {
//            _state.update { it.copy(isLoading = true, errorMessage = "") }
//            topicRepository.deleteTopic(topicId)
//                .onRight {
//                    _state.update { currentState ->
//                        val updatedTopics = currentState.topics.filterNot { it.id == topicId }
//                        currentState.copy(
//                            topics = updatedTopics,
//                            isLoading = false
//                        )
//                    }
//                }
//                .onLeft { failure ->
//                    _state.update {
//                        it.copy(
//                            isLoading = false,
//                            errorMessage = failure.message ?: "failed to delete topic"
//                        )
//                    }
//                }
//        }
    }

    private fun startMultiSelect(topicId: Int) {
        _state.update {
            it.copy(
                isMultiSelecting = true,
                selectedTopics = setOf(topicId)
            )
        }
    }

    private fun cancelMultiSelect() {
        _state.update {
            it.copy(
                isMultiSelecting = false,
                isSelectAll = false,
                selectedTopics = emptySet()
            )
        }
    }

    private fun selectAll() {
        _state.update { currentState ->
            val allSelected = currentState.isSelectAll
            currentState.copy(
                selectedTopics = if (allSelected) emptySet() else currentState.topics.map { it.id }.toSet(),
                isSelectAll = !allSelected
            )
        }
    }

    private fun toggleTopicSelection(topicId: Int) {
        val currentSelected = _state.value.selectedTopics.toMutableSet()
        if (currentSelected.contains(topicId)) {
            currentSelected.remove(topicId)
        } else {
            currentSelected.add(topicId)
            }
        _state.update { currentState ->
            currentState.copy(
                selectedTopics = currentSelected,
                isSelectAll = currentSelected.size == currentState.topics.size
            )
        }
    }

    private fun deleteSelectedTopics() {

    }
}