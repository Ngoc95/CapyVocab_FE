package com.example.capyvocab_fe.admin.dashboard.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.dashboard.domain.model.CourseStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.FolderStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.RevenueStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopicStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.UserStats
import com.example.capyvocab_fe.core.error.AppFailure

interface DashboardRepository {
    suspend fun getRevenueSummary(): Either<AppFailure, RevenueStats>
    suspend fun getUserSummary(): Either<AppFailure, UserStats>
    suspend fun getCourseSummary(): Either<AppFailure, CourseStats>
    suspend fun getTopicSummary(): Either<AppFailure, TopicStats>
    suspend fun getFolderSummary(): Either<AppFailure, FolderStats>
}

