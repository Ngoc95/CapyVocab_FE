package com.example.capyvocab_fe.admin.user.presentation.users_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserCard(
    user: User,
    isExpanded: Boolean,
    isMultiSelecting: Boolean,
    isSelected: Boolean,
    onExpandToggle: () -> Unit,
    onEditClick:() -> Unit,
    onLongClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    cardElevation: Dp = 8.dp
) {
    var expanded by remember { mutableStateOf(isExpanded) }

    //animation for checkbox
    val checkboxScale = animateFloatAsState(
        targetValue = if (isMultiSelecting) 1f else 0f,
        animationSpec = tween(300),
        label = "checkboxScale"
    )

    val cardColor = animateColorAsState(
        targetValue = if (isSelected && isMultiSelecting) Color(0xFF91DFFF) else Color(0xFF00D9FF),
        animationSpec = tween(300),
        label = "cardColor"
    )

    val detailBgColor = animateColorAsState(
        targetValue = if (isSelected && isMultiSelecting) Color(0xFFCCF2FF) else Color(0xFFB5EEFF),
        animationSpec = tween(300),
        label = "detailBgColor"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if(isMultiSelecting) {
                        onCheckedChange(!isSelected)
                    } else {
                        expanded = !expanded
                        onExpandToggle()
                    }
                },
                onLongClick = {
                    onLongClick()
                }
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (isMultiSelecting) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onCheckedChange(it) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor.value), // màu header
            elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
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
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.default_avt),
                            error = painterResource(R.drawable.default_avt),
                            fallback = painterResource(R.drawable.default_avt)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = user.username,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF413B38),
                                    modifier = Modifier.weight(1f, fill = false)
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                val (bgColor, text, textColor) = when (user.roleId) {
                                    1 -> Triple(Color.Gray, "Admin", Color.White)
                                    2 -> Triple(Color(0xFF0DFF00), "Miễn phí", Color(0xFF125C00))
                                    3 -> Triple(Color(0xFFFFE0F0), "Premium", Color(0xFFDF1E71))
                                    else -> Triple(Color.LightGray, "Không rõ", Color.Black)
                                }

                                Box(
                                    modifier = Modifier
                                        .shadow(4.dp, RoundedCornerShape(20.dp), clip = false)
                                        .background(bgColor, RoundedCornerShape(20.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = text,
                                        color = textColor,
                                        style = MaterialTheme.typography.labelSmall
                                    )
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
                            .background(
                                detailBgColor.value,
                                RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
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

//@Preview(showBackground = true)
////@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun UserCardPreview() {
//    CapyVocab_FETheme {
//        UserCard(
//            user = User(
//                id = 1,
//                email = "duongkhanhngoc@gmail.com",
//                username = "Snoopy",
//                avatar = "https://i.pinimg.com/736x/50/f4/fb/50f4fb7f863bfcfa8afcf424882d216c.jpg", // dùng placeholder
//                status = "VERIFIED",
//                streak = 20,
//                lastStudyDate = "10/04/2025",
//                totalStudyDay = 20,
//                totalLearnedCard = 100,
//                totalMasteredCard = 70,
//                roleId = 2,
//            ),
//            isExpanded = true,
//            onExpandToggle = {},
//            onEditClick = {}
//        )
//    }
//}