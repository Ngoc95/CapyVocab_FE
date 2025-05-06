package com.example.capyvocab_fe.admin.topic.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.capyvocab_fe.admin.course.domain.model.TopicInCourse
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.core.ui.components.Badge
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

@Composable
fun TopicCard(
    topic: TopicInCourse,
    onClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        // Đổ bóng
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 6.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(24.dp)
                )
                .blur(20.dp)
        )

        // Card chính
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF00D9FF), Color(0xFFBEEBF9))
                        )
                    )
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = topic.thumbnail,
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = topic.title,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF413B38),
                                modifier = Modifier.weight(1f, fill = false),
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Badge(text = topic.type, textColor = Color(0xFF125C00), backgroundColor = Color(0xFF00FF00))
                        }

                        topic.description?.let {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF413B38)
                            )
                        }
                    }

                    IconButton(
                        onClick = { onEditClick() },
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.Top)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color(0xFF5E4A45)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopicCardPreview() {
    CapyVocab_FETheme {
        val sampleTopic = TopicInCourse(
            id = 1,
            title = "Friendship",
            description = "Tình bạn",
            thumbnail = null,
            type = "Free",
            deletedAt = null,
            createdAt = "",
            updatedAt = "",
            displayOrder = 1
        )

        TopicCard(
            topic = sampleTopic,
            onClick = {},
            onEditClick = {}
        )
    }
}
