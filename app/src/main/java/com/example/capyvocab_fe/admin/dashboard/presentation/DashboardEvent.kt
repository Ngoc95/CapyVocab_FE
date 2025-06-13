package com.example.capyvocab_fe.admin.dashboard.presentation

sealed class DashboardEvent {
    data class SelectTab(val tab: DashboardTab): DashboardEvent()
    object LoadRevenueStats : DashboardEvent()
    object LoadUserStats : DashboardEvent()
    object LoadCourseStats : DashboardEvent()
    object LoadTopicStats : DashboardEvent()
    object LoadFolderStats : DashboardEvent()
}
