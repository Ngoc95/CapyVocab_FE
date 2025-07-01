package com.example.capyvocab_fe.admin.navigator.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
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
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.dimens

@Composable
fun AdminNavigationDrawerContent(
    items: List<DrawerNavigationItem>,
    currentRoute: String,
    onItemClick: (String) -> Unit,
    onLogoutClick: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(vertical = MaterialTheme.dimens.small3)
        ) {
            // App logo and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.small3, vertical = MaterialTheme.dimens.medium2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(MaterialTheme.dimens.logoSize),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))
                Text(
                    text = "CapyVocab",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Divider(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small3, vertical = MaterialTheme.dimens.small1),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))

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

            // Add divider before logout button
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
            Divider(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small3, vertical = MaterialTheme.dimens.small1),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
            // Logout button
            DrawerLogoutItem(onClick = onLogoutClick)
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
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.surface
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.small3, vertical = MaterialTheme.dimens.small1)
            .clip(RoundedCornerShape(MaterialTheme.dimens.small2))
            .border(
                width = 0.7.dp,
                color = borderColor,
                shape = RoundedCornerShape(MaterialTheme.dimens.small2)
            )
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(MaterialTheme.dimens.small3)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = if (isSelected) item.selectedIconRes else item.iconRes),
                contentDescription = item.title,
                modifier = Modifier.size(MaterialTheme.dimens.medium2),
                tint = textColor
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

            Text(
                text = item.title,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
        }
    }
}

@Composable
fun DrawerLogoutItem(onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = MaterialTheme.dimens.small3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Đăng xuất",
                modifier = Modifier.size(MaterialTheme.dimens.medium2),
                tint = Color.Red
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

            Text(
                text = "Đăng xuất",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
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
    CapyVocab_FETheme(dynamicColor = false) {
        Surface(
            modifier = Modifier.width(280.dp),
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
                        route = Route.ProfileScreen.route,
                        iconRes = R.drawable.user_profile,
                        selectedIconRes = R.drawable.ic_selected_profile
                    )
                ),
                currentRoute = Route.CoursesScreen.route,
                onItemClick = {},
                onLogoutClick = {}
            )
        }
    }
}