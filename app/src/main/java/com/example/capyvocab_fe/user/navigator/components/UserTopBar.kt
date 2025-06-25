package com.example.capyvocab_fe.user.navigator.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.dimens
import com.example.capyvocab_fe.user.notification.presentation.components.NotificationBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTopBar(
    navController: NavController,
    notificationCount: Int
) {
    TopAppBar(
        title = {
            Text(
                text = "CapyVocab",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF03A9F4)
            )
        },
        actions = {
            Box(
                modifier = Modifier
                    .padding(end = MaterialTheme.dimens.small3)
                    .size(MaterialTheme.dimens.medium3 * 1.5f)
                    .clickable {
                        navController.navigate(Route.UserNotificationScreen.route)
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Thông báo",
                    tint = Color.LightGray,
                    modifier = Modifier.fillMaxSize()
                )

                NotificationBadge(
                    count = notificationCount,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 2.dp, y = (-2).dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}
