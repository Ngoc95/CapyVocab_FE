package com.example.capyvocab_fe.user.navigator.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.navigator.components.BottomNavigationItem

@Composable
fun UserBottomNavigation(
    items: List<BottomNavigationItem>,
    selected: Int,
    onItemClick: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            tonalElevation = 4.dp,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEachIndexed { index, item ->
                    if (index == 2) {
                        Spacer(modifier = Modifier.width(75.dp))
                    } else {
                        val isSelected = index == selected
                        val iconPainter = painterResource(id = if (isSelected) item.selectedIcon else item.icon)

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

        // Nút trung tâm – nằm ngoài NavigationBar, nổi lên
        val centerItem = items[2]
        val isSelected = selected == 2
        val iconPainter = painterResource(id = if (isSelected) centerItem.selectedIcon else centerItem.icon)

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp) // Điều chỉnh độ nổi lên
                .zIndex(1f)
                .clip(CircleShape)
                .background(Color(0xFFCCE5FF))
                .clickable { onItemClick(2) }
                .padding(vertical = 8.dp, horizontal = 12.dp), // co gọn vùng bo tròn
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = iconPainter,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = centerItem.text,
                    fontSize = 11.sp,
                    color = if (isSelected) Color(0xFF007BFF) else Color.Gray
                )
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
        BottomNavigationItem(icon = R.drawable.user_community, selectedIcon = R.drawable.user_selected_community, text = "Cộng đồng"),
        BottomNavigationItem(icon = R.drawable.user_review, selectedIcon = R.drawable.user_selected_review, text = "Ôn tập"),
        BottomNavigationItem(icon = R.drawable.user_learn, selectedIcon = R.drawable.user_selected_learn, text = "Học từ vựng"),
        BottomNavigationItem(icon = R.drawable.user_test, selectedIcon = R.drawable.user_selected_test, text = "Kiểm tra"),
        BottomNavigationItem(icon = R.drawable.user_profile, selectedIcon = R.drawable.user_selected_profile, text = "Hồ sơ")
    ), selected = 4, onItemClick = {})
}