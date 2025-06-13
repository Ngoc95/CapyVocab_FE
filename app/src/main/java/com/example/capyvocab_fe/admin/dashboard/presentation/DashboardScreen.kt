package com.example.capyvocab_fe.admin.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capyvocab_fe.admin.dashboard.presentation.components.CourseScreen
import com.example.capyvocab_fe.admin.dashboard.presentation.components.FolderScreen
import com.example.capyvocab_fe.admin.dashboard.presentation.components.RevenueScreen
import com.example.capyvocab_fe.admin.dashboard.presentation.components.TopicScreen
import com.example.capyvocab_fe.admin.dashboard.presentation.components.UserScreen

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state = viewModel.state

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = state.selectedTab.ordinal
        ) {
            DashboardTab.values().forEachIndexed { index, tab ->
                Tab(
                    selected = state.selectedTab == tab,
                    onClick = {
                        // Chỉ gửi sự kiện nếu tab khác tab hiện tại
                        if (state.selectedTab != tab) {
                            viewModel.onEvent(DashboardEvent.SelectTab(tab))
                        }
                    },
                    text = { Text(tab.title) }
                )

            }
        }

        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Text(text = "Lỗi: ${state.error}", color = Color.Red)
            }

            else -> {
                when (state.selectedTab) {
                    DashboardTab.REVENUE -> {
                        state.revenueStats?.let { revenueStats ->
                            RevenueScreen(revenueStats)
                        } ?: Text("Đang tải dữ liệu...")
                    }

                    DashboardTab.USERS -> {
                        state.userStats?.let { userStats ->
                            UserScreen(userStats)
                        } ?: Text("Đang tải dữ liệu...")
                    }
                    DashboardTab.COURSES -> {
                        state.courseStats?.let {
                            CourseScreen(courseStats = it, topCourses = state.topCourseDetails)
                        } ?: Text("Đang tải dữ liệu...")
                    }
                    DashboardTab.TOPICS -> {
                        state.topicStats?.let {
                            TopicScreen(
                                stats = it,
                                popularTopics = state.popularTopicDetails
                            )
                        } ?: Text("Đang tải dữ liệu...")
                    }
                    DashboardTab.FOLDERS -> {
                        state.folderStats?.let {
                            FolderScreen(stats = it, topFolders = state.topFolderDetails)
                        } ?: Text("Đang tải dữ liệu...")
                    }
                }
            }
        }
    }
}
