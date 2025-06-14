package com.example.capyvocab_fe.user.navigator.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "CapyVocab",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF03A9F4)
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Thông báo",
                tint = Color.LightGray,
                modifier = Modifier
                    .size(MaterialTheme.dimens.medium3 * 1.5f)
                    .padding(end = MaterialTheme.dimens.small3)
                    .clickable {
                        //navController.navigate(Route.NotificationScreen.route)
                    }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}
