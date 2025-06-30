package com.example.capyvocab_fe.admin.topic.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopicCard(
    topic: Topic,
    isMultiSelecting: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onLongClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    cardElevation: Dp = 8.dp,
    isAdmin: Boolean
) {
    //animation for checkbox
    val checkboxScale = animateFloatAsState(
        targetValue = if (isMultiSelecting) 1f else 0f,
        animationSpec = tween(300),
        label = "checkboxScale"
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
            .padding(horizontal = MaterialTheme.dimens.small1, vertical = MaterialTheme.dimens.extraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .scale(checkboxScale.value)
                .width(MaterialTheme.dimens.medium2 * checkboxScale.value),
            contentAlignment = Alignment.Center
        ) {
            if (isMultiSelecting) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onCheckedChange(it) },
                    modifier = Modifier.padding(end = MaterialTheme.dimens.small1)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            // Đổ bóng
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(y = MaterialTheme.dimens.small1)
                    .background(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(MaterialTheme.dimens.medium2)
                    )
                    .blur(MaterialTheme.dimens.medium2)
            )

            // Card chính
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(MaterialTheme.dimens.medium2),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = if (topic.alreadyLearned)
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFB9FBC0), Color(0xFF70E000))
                                )
                            else
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF00D9FF), Color(0xFFBEEBF9))
                                )
                        )
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.small3)
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(topic.thumbnail)
                                .crossfade(true)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .size(300) // Hoặc kích thước phù hợp, ví dụ: 300px
                                .build(),
                            placeholder = painterResource(R.drawable.placeholder_img),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(MaterialTheme.dimens.large)
                                .clip(CircleShape)
                        )


                        Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = topic.title,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF2B2B2B),
                                    modifier = Modifier.weight(1f, fill = false),
                                )
                            }

                            topic.description?.let {
                                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF413B38)
                                )
                            }
                        }

                        if(isAdmin) {
                            IconButton(
                                onClick = { onEditClick() },
                                modifier = Modifier
                                    .size(MaterialTheme.dimens.medium3)
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
    }
}

@Preview(showBackground = true)
@Composable
fun TopicCardPreview() {
        val sampleTopic = Topic(
            id = 1,
            title = "Friendship",
            description = "Tình bạn",
            thumbnail = "",
            type = "Free",
            alreadyLearned = false
        )

        TopicCard(
            topic = sampleTopic,
            onClick = {},
            onEditClick = {},
            onLongClick = {},
            onCheckedChange = {},
            isMultiSelecting = false,
            isSelected = false,
            isAdmin = true
        )
}
