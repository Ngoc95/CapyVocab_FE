package com.example.capyvocab_fe.user.community.presentation.components


import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.community.domain.model.Comment
import com.example.capyvocab_fe.user.community.domain.model.TargetType
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CommentCard(
    comment: Comment,
)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp,1.dp)
    ) {
        // Avatar
        AsyncImage(
            model = comment.createdBy.avatar,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 4.dp,
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = comment.createdBy.username,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = comment.content)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

                Text(
                    text = sdf.format(comment.createdAt),
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentCardPreview()
{
    CapyVocab_FETheme {
        CommentCard(
            comment = Comment(
                id = 1,
                content = "Hi mọi người mình có cái bla bla cần blu blu. mong mng ble ble",
                createdBy = User(
                    id = 1,
                    username = "Aki",
                    roleId = 1,
                    email = "user@gmail.com",
                    avatar = null,
                ),
                targetId = 1,
                targetType = TargetType.POST,
                createdAt = SimpleDateFormat("dd/MM/yyyy").parse("26/05/2025"),
                updatedAt = null,
                parentComment = null,
            ),
        )
    }
}
