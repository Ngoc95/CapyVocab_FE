package com.example.capyvocab_fe.user.notification.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.core.data.TokenManager
import com.example.capyvocab_fe.user.notification.domain.model.Notification
import com.example.capyvocab_fe.user.notification.domain.usecase.GetNotificationsUseCase
import com.example.capyvocab_fe.user.notification.domain.usecase.MarkNotificationAsReadUseCase
import com.example.capyvocab_fe.user.notification.domain.usecase.ObserveNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase,
    private val observeNotificationsUseCase: ObserveNotificationsUseCase,
    private val tokenManager: TokenManager
): ViewModel() {
    private val _state = MutableStateFlow(NotificationState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            tokenManager.userId.first()?.let { userId ->
                connectSocket(userId.toString())
                loadNotifications()
                observeNotificationsUseCase().collect { notification ->
                    handleNewNotification(notification)
                }
            }
        }
    }

    fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.LoadNotifications -> loadNotifications()
            is NotificationEvent.LoadMoreNotifications -> loadNotifications(loadMore = true)
            is NotificationEvent.MarkAsRead -> markAsRead(event.notificationId)
        }
    }

    private fun loadNotifications(loadMore: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val nextPage = if (loadMore) state.value.currentPage + 1 else 1
            getNotificationsUseCase(nextPage)
                .onRight { userNotifications ->
                    _state.update { state ->
                        val allNotifications = if (loadMore) {
                            (state.notifications + userNotifications).distinctBy { it.id }
                        } else {
                            userNotifications
                        }
                        state.copy(
                            notifications = allNotifications,
                            isLoading = false,
                            unreadCount = allNotifications.count { !it.alreadyRead },
                            currentPage = nextPage,
                            isEndReached = userNotifications.isEmpty()
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = failure.message ?: "Xảy ra lỗi khi tải thông báo"
                        )
                    }
                }
        }
    }

    private fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            markNotificationAsReadUseCase(notificationId)
                .onRight { updatedNotification ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            notifications = it.notifications.map { notification ->
                                if (notification.id == updatedNotification.id) updatedNotification else notification
                            },
                            unreadCount = it.unreadCount - 1
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update { it.copy(error = failure.message ?: "Xảy ra lỗi khi đánh dấu thông báo đã đọc") }
                }
        }
    }

    private fun handleNewNotification(notification: Notification) {
        loadNotifications()
    }
    private fun connectSocket(userId: String) {
        observeNotificationsUseCase.connect(userId)
    }

    private fun disconnectSocket() {
        observeNotificationsUseCase.disconnect()
    }

    override fun onCleared() {
        super.onCleared()
        disconnectSocket()
    }
}