package com.example.capyvocab_fe.admin.course.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.course.domain.repository.AdminCourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
            else -> Unit
        }
    }

    private fun loadCourses() {
        viewModelScope.launch {
            courseRepository.getAllCourses()
                .onRight { courses ->
                    _state.value = _state.value.copy(
                        courses = courses,
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
