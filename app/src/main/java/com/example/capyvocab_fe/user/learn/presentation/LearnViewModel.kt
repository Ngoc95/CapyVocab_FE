package com.example.capyvocab_fe.user.learn.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
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
            is LearnEvent.ClearTopics -> _state.update { it.copy(topics = emptyList()) }
            is LearnEvent.LoadWords -> loadWords(event.topic)
            is LearnEvent.FlipCard -> { _state.update { it.copy(isFlipped = !it.isFlipped) } }
            is LearnEvent.ContinueToTyping -> goToTyping()
            is LearnEvent.SubmitAnswer -> submitAnswer(event.answer)
            is LearnEvent.ContinueAfterResult -> continueAfterResult()
            is LearnEvent.AlreadyKnowWord -> skipCurrentWord()
            is LearnEvent.ClearWords -> _state.update { it.copy(words = emptyList(), currentIndex = 0, isComplete = false) }
            is LearnEvent.DismissCompletionDialog -> {
                _state.update { it.copy(showCompletionDialog = false) }
            }
            is LearnEvent.ClearError -> _state.update { it.copy(errorMessage = "") }
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

    private fun loadWords(topic: Topic) {
        val topicId = topic.id
        _state.update {
            it.copy(
                isLoading = true,
                errorMessage = "",
                selectedTopic = topic // cập nhật trước
            )
        }

        viewModelScope.launch {
            userLearnRepository.getTopicWords(topicId)
                .onRight { newWords ->
                    if (_state.value.selectedTopic?.id == topicId) {
                        _state.update {
                            it.copy(
                                words = newWords,
                                isLoading = false,
                                currentIndex = 0,
                                isTyping = false,
                                isLearning = true,
                                isComplete = false,
                                isFlipped = false,
                            )
                        }
                    }
                }
                .onLeft { failure ->
                    if (_state.value.selectedTopic?.id == topicId) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = failure.message ?: "failed to load words"
                            )
                        }
                    }
                }
        }
    }

    private fun goToTyping() {
        _state.update {
            it.copy(
                isTyping = true,
                isLearning = false,
                isFlipped = false
            )
        }
    }

    private var tempAnswerCorrect = false

    private fun submitAnswer(answer: String) {
        val currentState = state.value
        val index = currentState.currentIndex
        val words = currentState.words.toMutableList()

        if (index >= words.size) return

        val currentWord = words[index]
        val isCorrect = currentWord.content.equals(answer.trim(), ignoreCase = true)

        // Lưu kết quả tạm
        tempAnswerCorrect = isCorrect

        _state.update {
            it.copy(
                answerResult = isCorrect,
                showResult = true,
                isTyping = true,       // GIỮ nguyên typing view
                isLearning = false     // Tắt flashcard view
            )
        }
    }



    private fun skipCurrentWord() {
        val currentState = state.value
        val index = currentState.currentIndex
        val nextIndex = index + 1
        val isDone = nextIndex >= currentState.words.size

        _state.update {
            it.copy(
                currentIndex = nextIndex.coerceAtMost(currentState.words.lastIndex),
                isTyping = false,
                isLearning = !isDone,
                isComplete = isDone,
                isFlipped = false
            )
        }

        if (isDone) {
            _state.update {
                it.copy(showCompletionDialog = true)
            }

            if (currentState.selectedTopic?.alreadyLearned == false) {
                markTopicComplete()
            }
        }
    }

    private fun markTopicComplete() {
        val topicId = _state.value.selectedTopic?.id ?: return
        _state.update { it.copy(isMarkingComplete = true) }

        viewModelScope.launch {
            userLearnRepository.markTopicComplete(topicId)
                .onRight {
                    // Nếu vẫn đang ở đúng topic
                    if (_state.value.selectedTopic?.id == topicId) {
                        _state.update {
                            it.copy(
                                isMarkingComplete = false,
                                isComplete = true,
                                showCompletionDialog = true
                            )
                        }
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isMarkingComplete = false,
                            errorMessage = failure.message ?: "Không thể đánh dấu hoàn thành"
                        )
                    }
                }
        }
    }

    fun continueAfterResult() {
        val currentState = state.value
        val index = currentState.currentIndex
        val words = currentState.words.toMutableList()

        if (index >= words.size) return

        if (!tempAnswerCorrect) {
            val wrongWord = words.removeAt(index)
            words.add(wrongWord)
        }

        val nextIndex = if (tempAnswerCorrect) index + 1 else index
        val isDone = nextIndex >= words.size

        _state.update {
            it.copy(
                words = words,
                currentIndex = nextIndex.coerceAtMost(words.lastIndex),
                showResult = false,
                answerResult = null,
                isTyping = false,
                isLearning = !isDone,
                isComplete = isDone,
                isFlipped = false
            )
        }

        if (isDone) {
            _state.update { it.copy(showCompletionDialog = true) }
            if (currentState.selectedTopic?.alreadyLearned == false) {
                markTopicComplete()
            }
        }
    }

}