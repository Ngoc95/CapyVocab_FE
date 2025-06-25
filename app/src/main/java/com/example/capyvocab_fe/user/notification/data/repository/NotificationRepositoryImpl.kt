package com.example.capyvocab_fe.user.notification.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.user.notification.data.mapper.NotificationMapper
import com.example.capyvocab_fe.user.notification.data.source.NotificationRemoteDataSource
import com.example.capyvocab_fe.user.notification.data.source.NotificationSocketDataSource
import com.example.capyvocab_fe.user.notification.domain.model.Notification
import com.example.capyvocab_fe.user.notification.domain.model.UserNotification
import com.example.capyvocab_fe.user.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val remoteDataSource: NotificationRemoteDataSource,
    private val socketDataSource: NotificationSocketDataSource,
    private val mapper: NotificationMapper
): NotificationRepository {
    override suspend fun getUserNotifications(
        page: Int,
        limit: Int
    ): Either<AppFailure, List<UserNotification>> {
        return remoteDataSource.getUserNotifications(page, limit)
            .map { dtoList -> dtoList.map { mapper.mapToDomain(it) } }
    }

    override suspend fun markNotificationAsRead(notificationId: Int): Either<AppFailure, UserNotification> {
        return remoteDataSource.markNotificationAsRead(notificationId)
            .map { dto -> mapper.mapToDomain(dto) }
    }

    override fun connectSocket(userId: String) {
        socketDataSource.connect(userId)
    }

    override fun disconnectSocket() {
        socketDataSource.disconnect()
    }

    override fun observeNotifications(): Flow<Notification> {
        return socketDataSource.observeNotifications()
            .map { mapper.mapToDomain(it) }
    }
}