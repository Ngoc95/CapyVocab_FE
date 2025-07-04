package com.example.capyvocab_fe.user.notification.presentation.handler

import androidx.navigation.NavController
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.user.notification.domain.model.NotificationType
import com.example.capyvocab_fe.user.notification.domain.model.UserNotification
import javax.inject.Inject

class NotificationActionHandler @Inject constructor(
    private val navController: NavController
) {
    fun handleNotificationClick(notification: UserNotification) {
        when(notification.notification.type) {
            NotificationType.COMMENT -> handleCommentNotification(notification)
            NotificationType.VOTE -> handleVoteNotification(notification)
            NotificationType.ORDER -> handleOrderNotification(notification)
            NotificationType.CHANGE_PASSWORD -> handlePasswordNotification(notification)
            else -> { /* Unknown notification type */ }
        }
    }
    private fun handleCommentNotification(notification: UserNotification) {
        // Extract data from notification
        val data = notification.notification.data.data
        val targetId = (data?.get("targetId") as? Number)?.toInt()
        val targetType = data?.get("targetType") as? String

        // Navigate to the appropriate screen
        if (targetId != null && targetType != null) {
            when (targetType) {
                "POST" -> navController.navigate("${Route.UserPostScreen.route}/$targetId")
                "FOLDER" -> navController.navigate("${Route.CommentScreen.route}/$targetId")
                // Add other target types as needed
            }
        }
    }

    private fun handleVoteNotification(notification: UserNotification) {
        // Similar to comment notification handling
        val data = notification.notification.data.data
        val targetId = (data?.get("targetId") as? Number)?.toInt()
        val targetType = data?.get("targetType") as? String

        if (targetId != null && targetType != null) {
            when (targetType) {
                "POST" -> navController.navigate("${Route.UserPostScreen.route}/$targetId")
                "FOLDER" -> navController.navigate("${Route.TestDetailScreen.route}/$targetId")
                // Add other target types as needed
            }
        }
    }

    private fun handleOrderNotification(notification: UserNotification) {
//        val data = notification.notification.data.data
//        val orderId = ((data?.get("order") as? Map<*, *>)?.get("id") as? Number)?.toInt()
//
//        if (orderId != null) {
//            navController.navigate("order/$orderId") //phải trả cái folder id
//            //navController.navigate("${Route.TestDetailScreen.route}/$folderId")
//        }
        navController.navigate(Route.UserTestScreen.route) {
            // Reset back stack but keep the start destination
            popUpTo(navController.graph.startDestinationRoute ?: Route.UserLearnScreen.route) {
                saveState = false // Don't save state
            }
            launchSingleTop = true
        }
    }

    private fun handlePasswordNotification(notification: UserNotification) {
        // For password change notifications, you might want to show a dialog
        // or navigate to the security settings
        navController.navigate(Route.ProfileScreen.route) {
            popUpTo(navController.graph.startDestinationRoute ?: Route.UserLearnScreen.route) {
                saveState = false // Don't save state
            }
            launchSingleTop = true
        }
    }
}