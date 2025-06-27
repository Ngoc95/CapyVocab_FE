package com.example.capyvocab_fe.admin.dashboard.presentation

import com.example.capyvocab_fe.admin.dashboard.domain.model.CourseStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.FolderStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.RevenueStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopCourseDetail
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopFolderDetail
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopTopicDetail
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopicStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.UserStats

data class DashboardState(
    val selectedTab: DashboardTab = DashboardTab.REVENUE,
    val isLoading: Boolean = false,
    val error: String? = null,
    val revenueStats: RevenueStats? = null,
    val userStats: UserStats? = null,
    val courseStats: CourseStats? = null,
    val topCourseDetails: List<TopCourseDetail> = emptyList(),
    val topicStats: TopicStats? = null,
    val popularTopicDetails: List<TopTopicDetail> = emptyList(),
    val folderStats: FolderStats? = null,
    val topFolderDetails: List<TopFolderDetail> = emptyList()
)

enum class DashboardTab(val title: String) {
    REVENUE("Doanh thu"),
    USERS("Người dùng"),
    COURSES("Khóa học"),
    TOPICS("Chủ đề"),
    FOLDERS("Folder")
}
