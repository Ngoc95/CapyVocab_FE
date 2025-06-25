package com.example.capyvocab_fe.user.notification.domain.usecase

import com.example.capyvocab_fe.user.notification.domain.model.Notification
import com.example.capyvocab_fe.user.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<Notification> {
        return repository.observeNotifications()
    }
    fun connect(userId: String) {
        repository.connectSocket(userId)
    }
    fun disconnect() {
        repository.disconnectSocket()
    }
}