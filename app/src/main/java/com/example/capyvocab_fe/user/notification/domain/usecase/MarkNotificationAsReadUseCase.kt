package com.example.capyvocab_fe.user.notification.domain.usecase

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.user.notification.domain.model.UserNotification
import com.example.capyvocab_fe.user.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkNotificationAsReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: Int): Either<AppFailure, UserNotification> {
        return repository.markNotificationAsRead(notificationId)
    }
}