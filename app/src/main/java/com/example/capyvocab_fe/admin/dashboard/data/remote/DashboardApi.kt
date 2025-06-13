package com.example.capyvocab_fe.admin.dashboard.data.remote

import com.example.capyvocab_fe.admin.dashboard.domain.model.CourseStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.FolderStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.RevenueStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopicStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.UserStats
import com.example.capyvocab_fe.core.network.ApiResponse
import retrofit2.http.GET

interface DashboardApi {
    @GET("system-earning/summary")
    suspend fun getRevenueSummary(): ApiResponse<RevenueStats>

    @GET("users/summary")
    suspend fun getUserSummary(): ApiResponse<UserStats>

    @GET("courses/summary")
    suspend fun getCourseSummary(): ApiResponse<CourseStats>

    @GET("topics/summary")
    suspend fun getTopicSummary(): ApiResponse<TopicStats>

    @GET("exercise/summary")
    suspend fun getFolderSummary(): ApiResponse<FolderStats>
}
