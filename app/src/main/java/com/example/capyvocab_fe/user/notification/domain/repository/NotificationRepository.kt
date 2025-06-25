package com.example.capyvocab_fe.user.notification.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.user.notification.domain.model.Notification
import com.example.capyvocab_fe.user.notification.domain.model.UserNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun getUserNotifications(
        page: Int = 1,
        limit: Int = 10,
    ): Either<AppFailure, List<UserNotification>>

    suspend fun markNotificationAsRead(notificationId: Int): Either<AppFailure, UserNotification>
    fun connectSocket(userId: String)
    fun disconnectSocket()
    fun observeNotifications(): Flow<Notification>
}