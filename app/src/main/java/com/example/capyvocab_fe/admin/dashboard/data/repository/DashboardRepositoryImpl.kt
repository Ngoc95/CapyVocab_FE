package com.example.capyvocab_fe.admin.dashboard.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.dashboard.data.remote.DashboardApi
import com.example.capyvocab_fe.admin.dashboard.domain.model.CourseStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.FolderStats

import com.example.capyvocab_fe.admin.dashboard.domain.model.RevenueStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopicStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.UserStats
import com.example.capyvocab_fe.admin.dashboard.domain.repository.DashboardRepository
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val api: DashboardApi
) : DashboardRepository {

    override suspend fun getRevenueSummary(): Either<AppFailure, RevenueStats> {
        return Either.catch {
            api.getRevenueSummary().metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getUserSummary(): Either<AppFailure, UserStats> {
        return Either.catch {
            api.getUserSummary().metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getCourseSummary(): Either<AppFailure, CourseStats> {
        return Either.catch {
            api.getCourseSummary().metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getTopicSummary(): Either<AppFailure, TopicStats> {
        return Either.catch {
            api.getTopicSummary().metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getFolderSummary(): Either<AppFailure, FolderStats> {
        return Either.catch {
            api.getFolderSummary().metaData
        }.mapLeft { it.toAppFailure() }
    }
}

