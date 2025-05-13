package com.example.capyvocab_fe.user.navigator.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.navigator.components.BottomNavigationItem
@Composable
fun UserBottomNavigation(
    items: List<BottomNavigationItem>,
    selected: Int,
    onItemClick: (Int) -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.fillMaxWidth().navigationBarsPadding()
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selected

                // Icon cho trạng thái được chọn / không chọn
                val iconPainter = painterResource(id = if (isSelected) item.selectedIcon else item.icon)

                if (index == 2) {
                    // Mục giữa được nâng cao và bo tròn
                    Box(
                        modifier = Modifier
                            .offset(y = (-12).dp)
                            .size(75.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFCCE5FF))
                            .clickable { onItemClick(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = iconPainter,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = item.text,
                                fontSize = 11.sp,
                                color = if (isSelected) Color(0xFF007BFF) else Color.Gray
                            )
                        }
                    }
                } else {
                    // Các mục còn lại
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onItemClick(index) },
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = iconPainter,
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    text = item.text,
                                    fontSize = 11.sp,
                                    color = if (isSelected) Color(0xFF007BFF) else Color.Gray
                                )
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Unspecified,
                            unselectedIconColor = Color.Unspecified,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

// Example usage with preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun UserBottomBarPreview() {
    UserBottomNavigation(items = listOf(
        BottomNavigationItem(icon = R.drawable.user_home, selectedIcon = R.drawable.user_selected_home, text = "Trang chủ"),
        BottomNavigationItem(icon = R.drawable.user_learn, selectedIcon = R.drawable.user_selected_learn, text = "Học từ vựng"),
        BottomNavigationItem(icon = R.drawable.user_community, selectedIcon = R.drawable.user_selected_community, text = "Cộng đồng"),
        BottomNavigationItem(icon = R.drawable.user_test, selectedIcon = R.drawable.user_selected_test, text = "Kiểm tra"),
        BottomNavigationItem(icon = R.drawable.user_profile, selectedIcon = R.drawable.user_selected_profile, text = "Hồ sơ")
    ), selected = 4, onItemClick = {})
}