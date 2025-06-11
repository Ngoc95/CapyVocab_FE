package com.example.capyvocab_fe.admin.topic.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.course.data.remote.model.TopicRequest
import com.example.capyvocab_fe.admin.course.data.remote.model.UpdateCourseRequest
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
            is TopicEvent.LoadAllTopics -> loadAllTopics()
            is TopicEvent.LoadMoreAllTopics -> loadAllTopics(loadMore = true, query = state.value.searchQuery)
            is TopicEvent.LoadTopics -> loadTopics(event.course)
            is TopicEvent.LoadMoreTopics -> loadTopics(event.course, loadMore = true, query = state.value.searchQuery)
            is TopicEvent.DeleteTopic -> deleteTopic(event.topicId)

            is TopicEvent.UpdateTopic -> updateTopic(event.topic, event.thumbnailUri)
            is TopicEvent.CreateTopic -> createTopic(
                event.courseId,
                event.topic,
                event.thumbnailUri
            )

            is TopicEvent.GetTopicById -> getTopicById(event.topicId)
            is TopicEvent.CancelMultiSelect -> cancelMultiSelect()
            is TopicEvent.OnDeleteSelectedTopics -> deleteSelectedTopics()
            is TopicEvent.OnSelectAllToggle -> selectAll()
            is TopicEvent.OnTopicLongPress -> startMultiSelect(event.topicId)
            is TopicEvent.OnTopicSelectToggle -> toggleTopicSelection(event.topicId)
            is TopicEvent.OnSearch -> {
                val query = state.value.searchQuery
                val currentCourse = state.value.currentCourse
                if (currentCourse != null) {
                    loadTopics(currentCourse, query = query)
                } else {
                    loadAllTopics(query = query)
                }
            }

            is TopicEvent.OnSearchQueryChange -> { _state.update { it.copy(searchQuery = event.query) }}
        }
    }

    private fun loadAllTopics(loadMore: Boolean = false, query: String? = null) {
        viewModelScope.launch {
            val nextPage = if (loadMore) state.value.currentPage + 1 else 1
            _state.update { it.copy(isLoading = true, errorMessage = "", currentCourse = null) }
            topicRepository.getAllTopic(nextPage, title = if (query?.isNotEmpty() == true) query else null)
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

    private fun createTopic(
        courseId: Int,
        topic: Topic,
        thumbnailUri: Uri?
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "", successMessage = "") }

            val thumbnailUrl =
                uploadThumbnailIfNeeded(thumbnailUri, topic.thumbnail) ?: return@launch

            val createTopicRequest = CreateTopicRequest(
                topics = listOf(
                    CreateTopicBody(
                        title = topic.title,
                        description = topic.description,
                        thumbnail = thumbnailUrl,
                        type = topic.type,
                        courseIds = listOf(courseId)
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
                            successMessage = "Thêm chủ đề thành công",
                            topics = updatedTopics
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "failed to create topic",
                            successMessage = ""
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

    private fun loadTopics(course: Course, loadMore: Boolean = false, query: String? = null) {
        viewModelScope.launch {
            val nextPage = if (loadMore) state.value.currentPage + 1 else 1
            _state.update { it.copy(isLoading = true, errorMessage = "", currentCourse = course) }

            courseRepository.getCourseTopics(course.id, nextPage, title = if (query?.isNotEmpty() == true) query else null)
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

    private fun updateTopic(topic: Topic, thumbnailUri: Uri?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "", successMessage = "") }

            val thumbnailUrl =
                uploadThumbnailIfNeeded(thumbnailUri, topic.thumbnail) ?: return@launch

            val updateTopicRequest = UpdateTopicRequest(
                title = topic.title,
                description = topic.description,
                thumbnail = thumbnailUrl,
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
                            successMessage = "Cập nhật chủ đề thành công",
                            topics = updatedTopics
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "failed to update topic",
                            successMessage = ""
                        )
                    }
                }
        }
    }

    private fun deleteTopic(topicId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "", successMessage = "") }

            // check if in course context or main topic screen
            val currentCourse = state.value.currentCourse

            if (currentCourse != null) {
                val remainingTopics = currentCourse.topics
                    .filterNot { it.id == topicId }
                    .mapIndexed { index, topic ->
                        TopicRequest(topic.id, index + 1)
                    }

                val updateCourseRequest = UpdateCourseRequest(
                    title = currentCourse.title,
                    description = currentCourse.description,
                    target = currentCourse.target,
                    level = currentCourse.level,
                    topics = remainingTopics
                )

                courseRepository.updateCourse(currentCourse.id, updateCourseRequest)
                    .onRight {
                        _state.update { currentState ->
                            val updatedTopics = currentState.topics.filterNot { it.id == topicId }
                            currentState.copy(
                                topics = updatedTopics,
                                isLoading = false,
                                errorMessage = "",
                                successMessage = "Xoá chủ đề khỏi khoá học thành công"
                            )
                        }
                    }
                    .onLeft { failure ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = failure.message ?: "Không thể xoá chủ đề ra khỏi khoá học",
                                successMessage = ""
                            )
                        }
                    }
            } else {
                // case 1: in TopicScreen
                topicRepository.deleteTopic(topicId)
                    .onRight {
                        _state.update { currentState ->
                            val updatedTopics = currentState.topics.filterNot { it.id == topicId }
                            currentState.copy(
                                topics = updatedTopics,
                                isLoading = false,
                                errorMessage = "",
                                successMessage = "Xoá chủ đề thành công"
                            )
                        }
                    }
                    .onLeft { failure ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = failure.message ?: "Không thể xoá chủ đề",
                                successMessage = ""
                            )
                        }
                    }
            }
        }
    }

    private fun deleteSelectedTopics() {
        viewModelScope.launch {
            val selectedIds = state.value.selectedTopics
            if (selectedIds.isEmpty()) return@launch

            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }

            // check if in course context or main topic screen
            val currentCourse = state.value.currentCourse
            var hasError = false

            if (currentCourse != null) {
                // case 2: in TopicsInCourseScreen - remove topics from course
                val remainingTopics = currentCourse.topics
                    .filterNot { it.id in selectedIds }
                    // Reorder the remaining topics to ensure sequential displayOrder
                    .mapIndexed { index, topic ->
                        TopicRequest(topic.id, index + 1)
                    }
                val updateCourseRequest = UpdateCourseRequest(
                    title = currentCourse.title,
                    description = currentCourse.description,
                    target = currentCourse.target,
                    level = currentCourse.level,
                    topics = remainingTopics
                )

                courseRepository.updateCourse(currentCourse.id, updateCourseRequest)
                    .onLeft { hasError = true }
            } else {
                // case 1: in TopicScreen - delete topic from database
                selectedIds.forEach { topicId ->
                    topicRepository.deleteTopic(topicId)
                        .onLeft { hasError = true }
                }
            }

            //update UI state
            val remainingTopics = state.value.topics.filterNot { it.id in selectedIds }

            _state.update {
                it.copy(
                    topics = remainingTopics,
                    selectedTopics = emptySet(),
                    isMultiSelecting = false,
                    isLoading = false,
                    errorMessage = if (hasError) "Một số chủ đề không thể xoá" else ""
                )
            }
        }
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
                selectedTopics = if (allSelected) emptySet() else currentState.topics.map { it.id }
                    .toSet(),
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

    private suspend fun uploadThumbnailIfNeeded(uri: Uri?, currentThumbnail: String?): String? {
        if (uri == null) return currentThumbnail
        val uploadResult = topicRepository.uploadThumbnailImage(uri)
        return uploadResult.fold(
            ifLeft = { failure ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Upload ảnh thất bại: ${failure.message}"
                    )
                }
                null
            },
            ifRight = { url -> url }
        )

    }
}