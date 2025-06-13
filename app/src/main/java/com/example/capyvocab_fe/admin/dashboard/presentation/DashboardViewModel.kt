package com.example.capyvocab_fe.admin.dashboard.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.capyvocab_fe.admin.course.data.remote.AdminCourseApi
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopTopicDetail
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopCourseDetail
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopFolderDetail
import com.example.capyvocab_fe.admin.dashboard.domain.repository.DashboardRepository
import com.example.capyvocab_fe.admin.topic.data.remote.AdminTopicApi
import com.example.capyvocab_fe.user.test.data.remote.ExerciseApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository,
    private val courseApi: AdminCourseApi,
    private val topicApi: AdminTopicApi,
    private val folderApi: ExerciseApi
) : ViewModel() {

    var state by mutableStateOf(DashboardState())
        private set

    init {
        onEvent(DashboardEvent.LoadRevenueStats)
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.SelectTab -> {
                if (state.selectedTab != event.tab) {
                    state = state.copy(selectedTab = event.tab)
                    loadTabData(event.tab)
                }
            }
            DashboardEvent.LoadRevenueStats -> {
                loadRevenueStats()
            }
            DashboardEvent.LoadUserStats -> {
                loadUserStats()
            }
            DashboardEvent.LoadCourseStats -> {
                loadCourseStats()
            }
            DashboardEvent.LoadTopicStats -> {
                loadTopicStats()
            }
            DashboardEvent.LoadFolderStats -> {
                loadFolderStats()
            }
        }
    }

    private fun loadTabData(tab: DashboardTab) {
        when (tab) {
            DashboardTab.REVENUE -> {
                loadRevenueStats()
            }
            DashboardTab.USERS -> {
                loadUserStats()
            }
            DashboardTab.COURSES -> {
                loadCourseStats()
            }
            DashboardTab.TOPICS -> {
                loadTopicStats()
            }
            DashboardTab.FOLDERS -> {
                loadFolderStats()
            }
            else -> Unit
        }
    }

    private fun loadRevenueStats() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            when (val result = repository.getRevenueSummary()) {
                is Either.Right -> state = state.copy(revenueStats = result.value, isLoading = false)
                is Either.Left -> state = state.copy(error = result.value.message, isLoading = false)
            }
        }
    }

    private fun loadUserStats() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            when (val result = repository.getUserSummary()) {
                is Either.Right -> state = state.copy(userStats = result.value, isLoading = false)
                is Either.Left -> state = state.copy(error = result.value.message, isLoading = false)
            }
        }
    }

    private fun loadCourseStats() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            when (val result = repository.getCourseSummary()) {
                is Either.Right -> {
                    val courseStats = result.value

                    // Load khóa học
                    val courseDetails = courseStats.topCourses.topCourses.mapNotNull { item ->
                        val res = runCatching { courseApi.getCourseById(item.courseId).metaData }
                        res.getOrNull()?.let { course ->
                            TopCourseDetail(
                                title = course.title,
                                level = course.level,
                                learnerCount = item.learnerCount
                            )
                        }
                    }

                    state = state.copy(
                        courseStats = courseStats,
                        topCourseDetails = courseDetails,
                        isLoading = false
                    )
                }

                is Either.Left -> {
                    state = state.copy(error = result.value.message, isLoading = false)
                }
            }
        }
    }

    private fun loadTopicStats() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            when (val result = repository.getTopicSummary()) {
                is Either.Right -> {
                    val topicStats = result.value

                    // Lấy thêm thông tin từng topic
                    val topicDetails = topicStats.popularTopic.mapNotNull { item ->
                        val res = runCatching { topicApi.getTopicById(item.topic).metaData }
                        res.getOrNull()?.let { topic ->
                            TopTopicDetail(
                                id = topic.id,
                                title = topic.title,
                                wordCount = topic.words.size,
                                completeCount = item.completeCount
                            )
                        }
                    }

                    state = state.copy(
                        topicStats = topicStats,
                        popularTopicDetails = topicDetails,
                        isLoading = false
                    )
                }

                is Either.Left -> {
                    state = state.copy(error = result.value.message, isLoading = false)
                }
            }
        }
    }

    private fun loadFolderStats() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            when (val result = repository.getFolderSummary()) {
                is Either.Right -> {
                    val folderStats = result.value

                    val folderDetails = folderStats.topFolders.mapNotNull { item ->
                        val res = runCatching { folderApi.getFolderById(item.id).metaData }
                        res.getOrNull()?.let { folder ->
                            TopFolderDetail(
                                id = folder.id,
                                name = folder.name,
                                createdBy = folder.createdBy,
                                price = folder.price,
                                voteCount = folder.voteCount,
                                commentCount = folder.commentCount,
                                questionCount = if(folder.flashCards != null) folder.flashCards.size else 0,
                                attemptCount = item.attemptCount
                            )
                        }
                    }

                    state = state.copy(
                        folderStats = folderStats,
                        topFolderDetails = folderDetails,
                        isLoading = false
                    )
                }

                is Either.Left -> {
                    state = state.copy(error = result.value.message, isLoading = false)
                }
            }
        }
    }

}
