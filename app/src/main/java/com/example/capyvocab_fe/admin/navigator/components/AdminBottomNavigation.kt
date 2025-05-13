package com.example.capyvocab_fe.admin.navigator.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.navBarBackground

@Composable
fun BottomNavigation(
    items: List<BottomNavigationItem>,
    selected: Int,
    onItemClick: (Int) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.navBarBackground,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                modifier = Modifier.weight(1f),
                selected = selected == index,
                onClick = { onItemClick(index) },
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(
                            painter = painterResource(if (selected == index) item.selectedIcon else item.icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.text,
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.tertiary,
                    indicatorColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}

data class BottomNavigationItem(
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val text: String
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BottomNavigationPreview() {
    CapyVocab_FETheme {
        BottomNavigation(items = listOf(
            BottomNavigationItem(icon = R.drawable.admin_home, selectedIcon = R.drawable.admin_selected_home, text = "Trang chủ"),
            BottomNavigationItem(icon = R.drawable.admin_course, selectedIcon = R.drawable.admin_selected_course, text = "Khoá học"),
            BottomNavigationItem(icon = R.drawable.admin_topic, selectedIcon = R.drawable.admin_selected_topic, text = "Chủ đề"),
            BottomNavigationItem(icon = R.drawable.admin_word, selectedIcon = R.drawable.admin_selected_word, text = "Từ vựng"),
            BottomNavigationItem(icon = R.drawable.admin_user,selectedIcon = R.drawable.admin_selected_user, text = "Người dùng"),
            BottomNavigationItem(icon = R.drawable.ic_setting,selectedIcon = R.drawable.ic_selected_setting, text = "Cài đặt")
        ), selected = 5, onItemClick = {}
        )
    }
}