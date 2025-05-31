package com.example.capyvocab_fe.user.profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.core.ui.components.TopBarTitle
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.MyGray
import com.example.capyvocab_fe.ui.theme.Styles.LightBlueTextStyle
import com.example.capyvocab_fe.ui.theme.Styles.TextButtonModifier
import com.example.capyvocab_fe.ui.theme.White
import com.example.capyvocab_fe.user.learn.presentation.LearnViewModel
import com.example.capyvocab_fe.user.profile.presentation.Components.UserCard

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

}

@Composable
fun ProfileScreenContent(
    user: User,
    isLoading: Boolean,
    selectUser: () -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(White)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White
                    )
                    .padding(top = 12.dp, bottom = 4.dp, start = 12.dp, end = 12.dp)
            ) {
                TopBarTitle("CabyVocab")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MyGray
                    )
                    .padding(12.dp)

            ) {
                UserCard(
                    avatarUrl = user.avatar,
                    id = user.id,
                    email = user.email,
                    onClick = selectUser
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 0.5.dp,
                        color = Color.LightGray
                    )
                    .background(Color.White)
                    .clickable { onLogout() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Đăng xuất",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenContentPreview() {
    CapyVocab_FETheme {
        ProfileScreenContent(
            isLoading = false,
            user =  User(
                id = 1,
                username = "",
                email = "ddd@gmail.com",
                avatar = null,
                roleId = 3
            ),
            selectUser = {},
            onLogout = {},
        )

    }
}

