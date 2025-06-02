package com.example.capyvocab_fe.admin.course.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.course.data.remote.model.CreateCourseBody
import com.example.capyvocab_fe.admin.course.data.remote.model.CreateCourseRequest
import com.example.capyvocab_fe.admin.course.data.remote.model.UpdateCourseRequest
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.domain.repository.AdminCourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val courseRepository: AdminCourseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CourseListState())
    val state: StateFlow<CourseListState> = _state

    fun onEvent(event: CourseEvent) {
        when (event) {
            is CourseEvent.LoadCourses -> loadCourses()
            is CourseEvent.LoadMoreCourses -> loadCourses(loadMore = true)
            is CourseEvent.SaveCourse -> saveCourse(course = event.course)
            is CourseEvent.DeleteCourse -> deleteCourse(event.courseId)
            is CourseEvent.OnDeleteSelectedCourses -> deleteSelectedCourses()
            is CourseEvent.OnCourseLongPress -> startMultiSelect(event.courseId)
            is CourseEvent.OnCourseSelectToggle -> toggleCourseSelection(event.courseId)
            is CourseEvent.OnSelectAllToggle -> selectAll()
            is CourseEvent.CancelMultiSelect -> cancelMultiSelect()
            is CourseEvent.GetCourseById -> getCourseById(event.courseId)
            is CourseEvent.OnSearch -> loadCourses(query = state.value.searchQuery)
            is CourseEvent.OnSearchQueryChange -> {_state.update { it.copy(searchQuery = event.query) }}
        }
    }

    private fun getCourseById(courseId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            courseRepository.getCourseById(courseId)
                .onRight { course ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedCourse = course.copy(courseTopics = emptyList())
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

    private fun loadCourses(loadMore: Boolean = false, query: String? = null) {
        viewModelScope.launch {
            val nextPage = if (loadMore) state.value.currentPage + 1 else 1
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            courseRepository.getAllCourses(nextPage, title = if (query?.isNotEmpty() == true) query else null)
                .onRight { newCourses ->
                    _state.update {
                        val allCourses = if (loadMore) it.courses + newCourses else newCourses
                        it.copy(
                            isLoading = false,
                            courses = allCourses,
                            currentPage = nextPage,
                            isEndReached = newCourses.isEmpty()
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

    private fun saveCourse(course: Course) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            val result = if (course.id == 0) {
                // create new course (POST)
                val createCourseReq = CreateCourseRequest(
                    courses = listOf(
                        CreateCourseBody(
                            title = course.title,
                            level = course.level,
                            target = course.target,
                            description = course.description
                        )
                    )
                )
                courseRepository.createCourse(createCourseReq)
            } else {
                // update existing course (PUT)
                val updateCourseReq = UpdateCourseRequest(
                    title = course.title,
                    level = course.level,
                    target = course.target,
                    description = course.description
                )
                courseRepository.updateCourse(course.id, updateCourseReq)
            }

            result.fold(
                ifRight = { updatedCourse ->
                    _state.update { currentState ->
                        val updatedCourses = if (course.id == 0) {
                            currentState.courses + updatedCourse
                        } else {
                            currentState.courses.map {
                                if (it.id == updatedCourse.id) updatedCourse else it
                            }
                        }
                        currentState.copy(
                            isLoading = false,
                            errorMessage = "",
                            successMessage = "Lưu khóa học thành công",
                            courses = updatedCourses
                        )
                    }
                },
                ifLeft = { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Lỗi khi lưu khóa học",
                            successMessage = ""
                        )
                    }
                }
            )
        }
    }

    private fun deleteCourse(courseId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            courseRepository.deleteCourse(courseId)
                .onRight {
                    _state.update { currentState ->
                        val updatedList = currentState.courses.filterNot { it.id == courseId }
                        currentState.copy(
                            courses = updatedList,
                            isLoading = false,
                            errorMessage = "",
                            successMessage = "Xoá khóa học thành công"
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Xoá khóa học thất bại",
                            successMessage = ""
                        )
                    }
                }
        }
    }

    private fun startMultiSelect(courseId: Int) {
        _state.update {
            it.copy(
                isMultiSelecting = true,
                selectedCourses = setOf(courseId)
            )
        }
    }

    private fun toggleCourseSelection(courseId: Int) {
        val currentSelected = _state.value.selectedCourses.toMutableSet()
        if (currentSelected.contains(courseId)) {
            currentSelected.remove(courseId)
        } else {
            currentSelected.add(courseId)
        }
        _state.update { currentState ->
            currentState.copy(
                selectedCourses = currentSelected,
                isSelectAll = currentSelected.size == currentState.courses.size
            )
        }
    }

    private fun selectAll() {
        _state.update { currentState ->
            val allSelected = currentState.isSelectAll
            currentState.copy(
                selectedCourses = if (allSelected) emptySet() else currentState.courses.map { it.id }
                    .toSet(),
                isSelectAll = !allSelected
            )
        }
    }

    private fun cancelMultiSelect() {
        _state.update {
            it.copy(
                isMultiSelecting = false,
                selectedCourses = emptySet(),
                isSelectAll = false
            )
        }
    }

    private fun deleteSelectedCourses() {
        viewModelScope.launch {
            val selected = state.value.selectedCourses
            if (selected.isEmpty()) return@launch
            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }
            var hasError = false
            selected.forEach { courseId ->
                courseRepository.deleteCourse(courseId)
                    .onLeft { hasError = true }
            }
            val remainingCourses = state.value.courses.filterNot { it.id in selected }
            _state.update {
                it.copy(
                    courses = remainingCourses,
                    selectedCourses = emptySet(),
                    isMultiSelecting = false,
                    isLoading = false,
                    errorMessage = if (hasError) "Một số khóa học không thể xoá" else ""
                )
            }
        }
    }
}
