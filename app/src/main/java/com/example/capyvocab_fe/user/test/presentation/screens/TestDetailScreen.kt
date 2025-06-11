package com.example.capyvocab_fe.user.test.presentation.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.White
import com.example.capyvocab_fe.user.test.domain.model.Folder

@Composable
fun TestDetailScreen(
    modifier: Modifier = Modifier,
    folder: Folder,
    onBack: (() -> Unit)? = null,
    onVoteClick: (Int) -> Unit,
    onUnVoteClick: (Int) -> Unit,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(White)
    ) {
        // Nút quay lại
        if (onBack != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(onClick = { onBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Quay lại"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Test details
                Text(
                    text = folder.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
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

                Text(
                    text = folder.createdBy?.email.toString(),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .background(Color(0xFFEEEEEE), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${folder.flashCards?.size} từ vựng",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CopyCodeRow(folder)
            Spacer(modifier = Modifier.weight(1f))
            // vote
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = if (folder.isAlreadyVote) R.drawable.ic_liked else R.drawable.ic_like),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable{
                            if (folder.isAlreadyVote) {
                                onUnVoteClick(folder.id)
                            } else {
                                onVoteClick(folder.id)
                            }
                        }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = folder.voteCount.toString(),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButton(
                icon = R.drawable.ic_remember_card,
                text = "Thẻ ghi nhớ",
                onClick = { navController.navigate("${Route.FlashCardScreen.route}/${folder.id}") }
            )

            ActionButton(
                icon = R.drawable.ic_do_test,
                text = "Làm test",
                onClick = {
                    navController.navigate("${Route.QuizScreen.route}/${folder.id}")
                }
            )

            ActionButton(
                icon = R.drawable.ic_comment,
                text = "Nhận xét",
                onClick = { navController.navigate("${Route.CommentScreen.route}/${folder.id}") }
            )
        }
    }
}

@Composable
fun CopyCodeRow(folder: Folder) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val toastMessage = "Đã sao chép mã"

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.ic_copy),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(20.dp)
                .clickable {
                    // Handle copy to clipboard
                    clipboardManager.setText(AnnotatedString(folder.code))

                    // Hiển thị Toast thông báo
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = folder.code,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}


@Composable
fun ActionButton(
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(8.dp), clip = false)
            .background(Color(0xFFF3FAFF), RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = text,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestDetailContentPreview() {
    val sampleFolder = Folder(
        id = 1,
        name = "Test Folder",
        code = "test123",
        price = 10.0,
        createdBy = null,
        voteCount = 10,
        commentCount = 5,
        totalAttemptCount = 10,
        isAlreadyVote = false,
        quizzes = null,
        flashCards = null,
        comments = emptyList()
    )
    CapyVocab_FETheme {
        TestDetailScreen(
            folder = sampleFolder,
            onBack = {},
            onVoteClick = {},
            onUnVoteClick = {},
            navController = NavController(LocalContext.current)
        )
    }
}