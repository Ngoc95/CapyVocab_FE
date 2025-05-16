package com.example.capyvocab_fe.user.test.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.White

data class TestItem(
    val title: String,
    val author: String,
    val price: String,
    val participants: Int,
    val wordsCount: Int,
    val isFree: Boolean = false
)

@Composable
fun TestScreen() {
    var selectedTabIndex by remember { mutableStateOf(1) }
    val tabs = listOf("Làm test", "Nhập code", "Đã tạo", "Tạo mới")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .statusBarsPadding()
    ) {
        // Header với thông tin người dùng
        UserInfoHeader()
        
        // Thanh điều hướng chức năng
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { 
                        selectedTabIndex = index
                    },
                    modifier = Modifier.padding(vertical = 8.dp),
                    content = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Biểu tượng cho từng tab
                            Icon(
                                painter = when (index) {
                                    0 -> painterResource(id = R.drawable.user_test_do)
                                    1 -> painterResource(id = R.drawable.user_test_code)
                                    2 -> painterResource(id = R.drawable.user_test_created)
                                    else -> painterResource(id = R.drawable.user_test_create)
                                },
                                contentDescription = title,
                                modifier = Modifier.size(30.dp),
                                tint = Color.Black
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Văn bản dưới biểu tượng
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                color = if (selectedTabIndex == index) Color(0xFF42B3FF) else Color.Black
                            )
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        
        // Phần nội dung - mặc định là Làm test
        when (selectedTabIndex) {
            0 -> DoTestContent()
            1 -> EnterCodeContent()
            2 -> CreatedTestsContent()
            3 -> CreateTestContent()
        }
    }
}

@Composable
fun UserInfoHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo và tên ứng dụng
        Text(
            text = "CapyVocab",
            color = Color(0xFF42B3FF),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        
        // Icon thông báo
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
    
    // Thông tin người dùng
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB5EEFF))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ảnh đại diện
        Image(
            painter = painterResource(id = R.drawable.user_profile),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape),
            contentScale = ContentScale.Crop
        )
        
        // Thông tin ID và mô tả
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            InfoRow("ID", "abc1234444")
            InfoRow("Đã tham gia", "10 bài test")
        }
    }
}

@Composable
fun InfoRow(label: String, text: String) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 14.sp
            )) {
                append("$label: ")
            }
            withStyle(style = SpanStyle(
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                fontSize = 14.sp
            )) {
                append(text)
            }
        }
    )
}

@Preview
@Composable
private fun TestScreenPreview() {
    CapyVocab_FETheme {
        TestScreen()
    }
}