package com.example.capyvocab_fe.user.notification.data.source

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.user.notification.data.dto.UserNotificationDto
import com.example.capyvocab_fe.user.notification.data.remote.NotificationApi
import javax.inject.Inject

class NotificationRemoteDataSource @Inject constructor(
    private val notificationApi: NotificationApi
) {
    suspend fun getUserNotifications(
        page: Int = 1,
        limit: Int = 10
    ): Either<AppFailure, List<UserNotificationDto>> {
        return try {
            val response = notificationApi.getUserNotifications(page, limit)
            Either.Right(response.metaData.notifications)
        } catch (throwable: Throwable) {
            Either.Left(throwable.toAppFailure())
        }
    }

    suspend fun markNotificationAsRead(notificationId: Int): Either<AppFailure, UserNotificationDto> {
        return try {
            val response = notificationApi.markNotificationAsRead(notificationId)
            Either.Right(response.metaData)
        } catch (throwable: Throwable) {
            Either.Left(throwable.toAppFailure())
        }
    }
}