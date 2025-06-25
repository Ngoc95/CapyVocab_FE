package com.example.capyvocab_fe.user.notification.presentation

sealed class NotificationEvent {
    object LoadNotifications : NotificationEvent()
    object LoadMoreNotifications : NotificationEvent()
    data class MarkAsRead(val notificationId: Int) : NotificationEvent()
}
