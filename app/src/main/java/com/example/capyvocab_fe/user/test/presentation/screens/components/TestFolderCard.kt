package com.example.capyvocab_fe.user.test.presentation.screens.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.test.domain.model.Folder

@SuppressLint("DefaultLocale")
@Composable
fun TestFolderCard(
    folder: Folder,
    isOwner: Boolean,
    onClick: () -> Unit,
    onVoteClick: (Int) -> Unit,
    onUnVoteClick: (Int) -> Unit,
    onSettingClick: (Folder) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tiêu đề bài test
                Text(
                    text = folder.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                if (isOwner) {
                    IconButton(onClick = { onSettingClick(folder) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Cài đặt",
                            tint = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Số từ vựng
            Box(
                modifier = Modifier
                    .background(color = Color(0xFFCCCCCC), RoundedCornerShape(20.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${folder.flashCards?.size} từ vựng",
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Thông tin tác giả
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ảnh đại diện
                AsyncImage(
                    model = folder.createdBy?.avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.default_avt),
                    error = painterResource(R.drawable.default_avt),
                    fallback = painterResource(R.drawable.default_avt)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // tên tác giả
                Text(
                    text = folder.createdBy?.email.toString(),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // giá + lượt tham gia
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // price
                Text(
                    text = if (folder.price == 0.0) "Miễn phí" else "${String.format("%,.0f", folder.price)} đồng",
                    fontSize = 14.sp,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.weight(1f))
                // participants
                Text(
                    text = "Lượt tham gia: ${folder.totalAttemptCount}",
                    fontSize = 14.sp,
                    color = Color.Black,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Vote button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        if (folder.isAlreadyVote) {
                            onUnVoteClick(folder.id)
                        } else {
                            onVoteClick(folder.id)
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = if (folder.isAlreadyVote) R.drawable.ic_liked else R.drawable.ic_like),
                        contentDescription = "Vote",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(folder.voteCount.toString())
                            }
                        },
                        fontSize = 14.sp
                    )
                }

                // Comment count
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(folder.commentCount.toString())
                        }
                        append(" nhận xét")
                    },
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun TestFolderCardPreview() {
    val sampleFolder = Folder(
        id = 1,
        name = "Test Folder",
        code = "test123",
        price = 0.0,
        createdBy = null,
        voteCount = 10,
        commentCount = 5,
        totalAttemptCount = 100,
        isAlreadyVote = false,
        quizzes = null,
        flashCards = null,
        comments = emptyList()
    )
    CapyVocab_FETheme {
        TestFolderCard(
            folder = sampleFolder,
            isOwner = true,
            onClick = {},
            onVoteClick = {  },
            onUnVoteClick = {},
            onSettingClick = {}
        )
    }
}