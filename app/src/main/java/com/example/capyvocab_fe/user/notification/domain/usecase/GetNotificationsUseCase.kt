package com.example.capyvocab_fe.user.notification.domain.usecase

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.user.notification.domain.model.UserNotification
import com.example.capyvocab_fe.user.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(page: Int): Either<AppFailure, List<UserNotification>> {
        return repository.getUserNotifications()
    }
}