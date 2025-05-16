package com.example.capyvocab_fe.user.test.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors

@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Tìm kiếm") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        shape = RoundedCornerShape(30.dp),
        singleLine = true,
        colors = defaultTextFieldColors()
    )
}

@Composable
fun DoTestContent() {
    // Danh sách bài test mẫu
    val testItems = remember {
        listOf(
            TestItem(
                title = "Bài tập chương 3",
                author = "ddddd@gmail.com",
                price = "Miễn phí",
                participants = 200,
                wordsCount = 20,
                isFree = true
            ),
            TestItem(
                title = "Giao tiếp cơ bản",
                author = "ddddd@gmail.com",
                price = "18.000 đ",
                participants = 200,
                wordsCount = 20
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(testItems) { item ->
            TestItemCard(item)
        }
    }
}

@Composable
fun TestItemCard(item: TestItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isFree) Color.White else Color(0xFFE6E6FA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Tiêu đề bài test
            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            // Số từ vựng
            Text(
                text = "${item.wordsCount} từ vựng",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Thông tin tác giả và giá
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ảnh đại diện tác giả
                if (!item.isFree) {
                    Image(
                        painter = painterResource(id = R.drawable.user_profile),
                        contentDescription = "Author",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.author.first().toString(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Email tác giả
                Text(
                    text = item.author,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )

                // Giá
                Text(
                    text = item.price,
                    fontSize = 12.sp,
                    fontWeight = if (item.isFree) FontWeight.Normal else FontWeight.Bold,
                    color = if (item.isFree) Color.Gray else Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Thông tin lượt tham gia và số từ nhận xét
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Lượt tham gia
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.user_profile),
                        contentDescription = "Participants",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = " ${item.participants}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Số từ nhận xét
                Text(
                    text = "${item.wordsCount} nhận xét",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}