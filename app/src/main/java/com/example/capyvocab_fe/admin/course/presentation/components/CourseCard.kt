package com.example.capyvocab_fe.admin.course.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.domain.model.CourseLevel
import com.example.capyvocab_fe.core.ui.components.Badge
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CourseCard(
    course: Course,
    isMultiSelecting: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onLongClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    cardElevation: Dp = 8.dp
) {
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (isMultiSelecting) {
                        onCheckedChange(!isSelected)
                    } else {
                        onClick()
                    }
                },
                onLongClick = {
                    onLongClick()
                }
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .scale(checkboxScale.value)
                .width(48.dp * checkboxScale.value),
            contentAlignment = Alignment.Center
        ) {
            if (isMultiSelecting) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onCheckedChange(it) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor.value), // màu header
            elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
        ) {
            Column {
                // Header phần trên
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF00D9FF))
                        .padding(12.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Badge(
                                text = "Đã công bố",
                                textColor = Color.White,
                                backgroundColor = Color(0xFF075743)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            style = MaterialTheme.typography.labelMedium,
                            text = course.title,
                            color = Color(0xFF2B2B2B),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.TopEnd)
                            .clickable(onClick = onEditClick),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }

                // Info phần dưới
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE0F7FA))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    InfoRow(
                        icon = R.drawable.target,
                        label = "Mục tiêu",
                        value = course.target
                    )
                    InfoRow(
                        icon = R.drawable.description,
                        label = "Nội dung",
                        value = course.description ?: "Không có"
                    )
                    InfoRow(
                        icon = R.drawable.level,
                        label = "Trình độ",
                        value = mapLevel(course.level)
                    )
                }
            }
        }
    }
}

fun sampleCourses() = listOf(
    Course(
        id = 1,
        title = "1000 TỪ CƠ BẢN",
        level = CourseLevel.BEGINNER.value,
        target = "Củng cố nền tảng tiếng Anh",
        description = "Từ vựng nền tảng",
        courseTopics = emptyList()
    ),
    Course(
        id = 2,
        title = "Ngữ pháp cơ bản",
        level = CourseLevel.INTERMEDIATE.value,
        target = "Nắm chắc ngữ pháp nền tảng",
        description = "Ngữ pháp phổ biến trong tiếng Anh giúp người mới hiểu được cơ bản cấu trúc",
        courseTopics = emptyList()
    ),
    Course(
        id = 3,
        title = "Từ vựng nâng cao",
        level = CourseLevel.ADVANCE.value,
        target = "Mở rộng vốn từ chuyên sâu",
        description = "Từ vựng học thuật và chuyên ngành",
        courseTopics = emptyList()
    )
)

@Preview(showBackground = true)
@Composable
fun CourseCardPreview() {
    CapyVocab_FETheme {
        CourseCard(
            course = sampleCourses()[1],
            onClick = {},
            onEditClick = {},
            onLongClick = {},
            onCheckedChange = {},
            isMultiSelecting = false,
            isSelected = false
        )
    }

}

@Composable
fun InfoRow(
    icon: Int,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(33.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                    append("$label: ")
                }
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append(value)
                }
            },
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
        )
    }
}


fun mapLevel(level: String): String {
    return when (level) {
        "Beginner" -> "Sơ cấp"
        "Intermediate" -> "Trung cấp"
        "Advance" -> "Cao cấp"
        else -> "Không xác định"
    }
}

