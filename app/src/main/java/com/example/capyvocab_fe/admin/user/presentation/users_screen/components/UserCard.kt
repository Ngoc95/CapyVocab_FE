package com.example.capyvocab_fe.admin.user.presentation.users_screen.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

@Composable
fun UserCard(
    user: User,
    isFree: Boolean,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    onEditClick:() -> Unit
) {
    var expanded by remember { mutableStateOf(isExpanded) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF00D9FF)), // màu header
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            // Header
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    AsyncImage(
                        model = user.avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = user.username,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF413B38)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            if (isFree) {
                                Box(
                                    modifier = Modifier
                                        .shadow(4.dp, RoundedCornerShape(20.dp), clip = false)
                                        .background(Color(0xFF0DFF00), RoundedCornerShape(20.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Miễn phí",
                                        color = Color(0xFF125C00),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                        Text(text = "#${user.id}", fontSize = 14.sp, color = Color.DarkGray)
                    }

                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                expanded = !expanded
                                onExpandToggle()
                            }
                    )
                }
            }

            // Detail
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFB5EEFF), RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)) // 👈 màu khác phần header
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        InfoRow("Email", user.email)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { onEditClick() }
                        )
                    }
                    InfoRow("Chuỗi", "${user.streak} ngày")
                    InfoRow("Ngày học cuối", user.lastStudyDate)
                    InfoRow("Tổng ngày học", "${user.totalStudyDay}")
                    InfoRow("Tổng thẻ đã học", "${user.totalLearnedCard}")
                    InfoRow("Tổng thẻ đã thành thạo", "${user.totalMasteredCard}")
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$label: ")
            }
            append(value)
        },
        fontSize = 14.sp,
        color = Color(0xFF413B38),
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

@Preview(showBackground = true)
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UserCardPreview() {
    CapyVocab_FETheme {
        UserCard(
            user = User(
                id = 1,
                email = "duongkhanhngoc@gmail.com",
                username = "Snoopy",
                password = "123456",
                avatar = "https://i.pinimg.com/736x/50/f4/fb/50f4fb7f863bfcfa8afcf424882d216c.jpg", // dùng placeholder
                status = "VERIFIED",
                streak = 20,
                lastStudyDate = "10/04/2025",
                totalStudyDay = 20,
                totalLearnedCard = 100,
                totalMasteredCard = 70,
                roleId = 1,
                fullName = "Nguyễn Văn A"
            ),
            isFree = true,
            isExpanded = true,
            onExpandToggle = {},
            onEditClick = {}
        )
    }
}


//data class User(
//    val id: Int,
//    val email: String,
//    val username: String,
//    val password: String,
//    val avatar: String,
//    val status: Int,
//    val streak: Int,
//    val lastStudyDate: String, // hoặc LocalDate nếu xử lý bằng Java time API
//    val totalStudyDay: Int,
//    val totalLearnedCard: Int,
//    val totalMasteredCard: Int,
//    val roleId: Int
//)
