package com.example.capyvocab_fe.admin.navigator.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

@Composable
fun AdminNavigationDrawerContent(
    items: List<DrawerNavigationItem>,
    currentRoute: String,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // App logo and title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.app_icon), // Replace with your app logo
                contentDescription = "App Logo",
                modifier = Modifier.size(36.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "CapyVocab",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation items
        items.forEach { item ->
            val isSelected = when {
                // Check if this is the exact route
                currentRoute == item.route -> true
                // Check if this is a parent route (for topics, words screens)
                item.route == Route.TopicsScreen.route && currentRoute?.startsWith("${Route.TopicsScreen.route}/") == true -> true
                item.route == Route.WordsScreen.route && currentRoute?.startsWith("${Route.WordsScreen.route}/") == true -> true
                else -> false
            }

            DrawerNavigationItem(
                item = item,
                isSelected = isSelected,
                onClick = { onItemClick(item.route) }
            )
        }
    }
}

@Composable
fun DrawerNavigationItem(
    item: DrawerNavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = if (isSelected) item.selectedIconRes else item.iconRes),
                contentDescription = item.title,
                modifier = Modifier.size(24.dp),
                tint = textColor
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = item.title,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }
}

data class DrawerNavigationItem(
    val title: String,
    val route: String,
    @DrawableRes val iconRes: Int,
    @DrawableRes val selectedIconRes: Int
)

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AdminNavigationDrawerPreview() {
    CapyVocab_FETheme {
        Surface(
            modifier = Modifier.width(280.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            AdminNavigationDrawerContent(
                items = listOf(
                    DrawerNavigationItem(
                        title = "Trang chủ",
                        route = Route.HomeScreen.route,
                        iconRes = R.drawable.admin_home,
                        selectedIconRes = R.drawable.admin_selected_home
                    ),
                    DrawerNavigationItem(
                        title = "Khoá học",
                        route = Route.CoursesScreen.route,
                        iconRes = R.drawable.admin_course,
                        selectedIconRes = R.drawable.admin_selected_course
                    ),
                    DrawerNavigationItem(
                        title = "Chủ đề",
                        route = Route.TopicsScreen.route,
                        iconRes = R.drawable.admin_topic,
                        selectedIconRes = R.drawable.admin_selected_topic
                    ),
                    DrawerNavigationItem(
                        title = "Từ vựng",
                        route = Route.WordsScreen.route,
                        iconRes = R.drawable.admin_word,
                        selectedIconRes = R.drawable.admin_selected_word
                    ),
                    DrawerNavigationItem(
                        title = "Người dùng",
                        route = Route.UsersScreen.route,
                        iconRes = R.drawable.admin_user,
                        selectedIconRes = R.drawable.admin_selected_user
                    ),
                    DrawerNavigationItem(
                        title = "Hồ sơ",
                        route = Route.SettingScreen.route,
                        iconRes = R.drawable.user_profile,
                        selectedIconRes = R.drawable.ic_selected_profile
                    )
                ),
                currentRoute = Route.CoursesScreen.route,
                onItemClick = {}
            )
        }
    }
}