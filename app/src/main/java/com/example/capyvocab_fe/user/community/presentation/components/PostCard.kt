package com.example.capyvocab_fe.user.community.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.MyLightBlue
import com.example.capyvocab_fe.ui.theme.Styles.LightBlueTextStyle
import com.example.capyvocab_fe.ui.theme.Styles.TextButtonModifier
import com.example.capyvocab_fe.ui.theme.dimens
import com.example.capyvocab_fe.user.community.domain.model.Post
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PostCard(
    post:Post,
    onPostComment: () -> Unit,
    onVoteClick: () -> Unit,
    onImageClick:(String) -> Unit,
    onClickUserPostsScreen:(User) -> Unit,
    onReportClick: () -> Unit
)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 2.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = post.createdBy.avatar,
                    contentDescription = "Avatar",
                    placeholder = painterResource(id = R.drawable.default_avt),
                    fallback = painterResource(id = R.drawable.default_avt),
                    error = painterResource(id = R.drawable.default_avt),
                    modifier = Modifier
                        .size(MaterialTheme.dimens.medium2)
                        .clip(CircleShape)
                        .clickable{ onClickUserPostsScreen(post.createdBy) }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = post.createdBy.username,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable{ onClickUserPostsScreen(post.createdBy) },
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

                    Text(
                        text = sdf.format(post.createdAt),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))

            Text(
                text = post.tags?.joinToString("  ") { "#$it" } ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = MyLightBlue,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))

            Text(
                text = post.content ?: "",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

            PostThumbsGrid(
                images = post.thumbnails,
                onImageClick = onImageClick
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))


            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${post.commentCount} trả lời, ${post.voteCount} lượt thích",
                        color = MyLightBlue,
                        style = MaterialTheme.typography.titleSmall
                    )

                    IconToggleButton(
                        checked = post.isAlreadyVote,
                        onCheckedChange = {
                            onVoteClick()
                        },
                        Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = if (post.isAlreadyVote) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (post.isAlreadyVote) "Liked" else "Like",
                            tint = if (post.isAlreadyVote) Color.Red else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = onPostComment,
                    modifier = TextButtonModifier,
                ) {
                    Text("TRẢ LỜI", style = LightBlueTextStyle)
                }

                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = onReportClick,
                    modifier = TextButtonModifier
                ) {
                    Text("BÁO CÁO", style = MaterialTheme.typography.labelLarge.copy(color = Color.Gray))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopicCardPreview()
{
    CapyVocab_FETheme {
        PostCard(
            post = Post(
                id = 1,
                content = "Hi mọi người mình có cái bla bla cần blu blu. mong mng ble ble",
                thumbnails = listOf("1", "2"),
                tags = listOf("IELTS", "Listen"),
                commentCount = 11,
                voteCount = 200,
                createdBy = User(
                    id = 1,
                    username = "Aki",
                    roleId = 1,
                    email = "user@gmail.com",
                    avatar = null,
                ),
                createdAt = SimpleDateFormat("dd/MM/yyyy").parse("26/05/2025"),
                updatedAt = null,
                isAlreadyVote = true,
            ),
            onPostComment = { },
            onVoteClick = { },
            onImageClick = { },
            onClickUserPostsScreen = { },
            onReportClick = { }
        )
    }
}