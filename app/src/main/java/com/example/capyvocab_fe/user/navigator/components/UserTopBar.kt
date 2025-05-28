package com.example.capyvocab_fe.user.navigator.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.capyvocab_fe.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "CapyVocab",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color(0xFF03A9F4)
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Thông báo",
                tint = Color.LightGray,
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 16.dp)
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
