package com.example.capyvocab_fe.user.community.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.MyLightBlue
import com.example.capyvocab_fe.ui.theme.Styles.LightBlueTextStyle
import com.example.capyvocab_fe.ui.theme.Styles.MyLikeButton
import com.example.capyvocab_fe.ui.theme.Styles.TextButtonModifier

@Composable
fun PostCard(
    avatarUrl: String,
    userName: String,
    content: String,
    replyCount: Int,
    likeCount: Int,
    onFollowClick: () -> Unit,
    onReplyClick: () -> Unit,
    onLikeClick: () -> Unit
)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = onFollowClick,
                    modifier = TextButtonModifier)
                {
                    Text("THEO DÕI", style = LightBlueTextStyle)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nội dung
            Text(
                text = content,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$replyCount trả lời",
                        color = MyLightBlue,
                        fontSize = 12.sp
                    )

                    MyLikeButton(
                        liked = false,
                        onLikedChange = { }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = onReplyClick,
                    modifier = TextButtonModifier,
                ) {
                    Text("TRẢ LỜI", style = LightBlueTextStyle)
                }

                Spacer(modifier = Modifier.width(4.dp))

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopicCardPreview()
{
    CapyVocab_FETheme {
        val sampleTopic = Topic(
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

        PostCard(
            avatarUrl = "https://link-to-avatar.jpg",
            userName = "ArigaName",
            content = "Hi mọi người mình có cái bla bla cần blu blu. mong mng ble ble",
            replyCount = 11,
            likeCount = 200,
            onFollowClick = { },
            onReplyClick = { },
            onLikeClick = { },
        )
    }
}