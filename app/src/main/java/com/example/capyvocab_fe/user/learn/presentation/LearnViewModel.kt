package com.example.capyvocab_fe.user.learn.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.user.learn.domain.repository.UserLearnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearnViewModel @Inject constructor(
    private val userLearnRepository: UserLearnRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LearnState())
    val state: StateFlow<LearnState> = _state

    fun onEvent(event: LearnEvent) {
        when (event) {
            is LearnEvent.LoadCourses -> loadCourses()
            is LearnEvent.LoadMoreCourses -> loadCourses(loadMore = true)
            is LearnEvent.GetCourseById -> getCourseById(event.courseId)
            is LearnEvent.LoadTopics -> loadTopics(event.course)
            is LearnEvent.LoadMoreTopics -> loadTopics(event.course, loadMore = true)
            is LearnEvent.GetTopicById -> getTopicById(event.topicId)
        }
    }

    private fun getCourseById(courseId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            userLearnRepository.getCourseById(courseId)
                .onRight { course ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedCourse = course
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Failed to load course"
                        )
                    }
                }
        }
    }

    private fun loadCourses(loadMore: Boolean = false) {
        viewModelScope.launch {
            val nextPage = if (loadMore) state.value.currentCoursePage + 1 else 1
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            userLearnRepository.getAllCourses(nextPage)
                .onRight { newCourses ->
                    _state.update {
                        val allCourses = if (loadMore) it.courses + newCourses else newCourses
                        it.copy(
                            isLoading = false,
                            courses = allCourses,
                            currentCoursePage = nextPage,
                            isEndReachedCourse = newCourses.isEmpty()
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

    private fun getTopicById(topicId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            userLearnRepository.getTopicById(topicId)
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
            val nextPage = if (loadMore) state.value.currentTopicPage + 1 else 1
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            userLearnRepository.getCourseTopics(course.id, nextPage)
                .onRight { newTopics ->
                    _state.update {
                        val allTopics = if (loadMore) it.topics + newTopics else newTopics
                        it.copy(
                            isLoading = false,
                            topics = allTopics,
                            currentTopicPage = nextPage,
                            isEndReachedTopic = newTopics.isEmpty()
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Failed to load topics"
                        )
                    }
                }
        }
    }
}