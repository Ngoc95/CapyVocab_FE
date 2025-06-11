package com.example.capyvocab_fe.user.profile.presentation

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.capyvocab_fe.user.community.presentation.CommunityEvent
import com.example.capyvocab_fe.user.community.presentation.CommunityScreen
import com.example.capyvocab_fe.user.learn.presentation.LearnViewModel
import com.example.capyvocab_fe.user.profile.domain.model.ProfileUser
import com.example.capyvocab_fe.user.profile.presentation.Components.UserCard
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(
    onSettingUser:(ProfileUser) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var visibleError by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        viewModel.onEvent(ProfileEvent.GetCurrentUser)
    }


    // Khi errorMessage thay đổi, show snackbar trong 3 giây
    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            visibleError = state.errorMessage
            delay(3000) // hiện 3 giây
            visibleError = "" // ẩn sau 3 giây
        }
    }

    state.currentUser?.let { user ->
        ProfileScreenContent (
            user = user,
            onSettingUser = { onSettingUser(user) },
        )
    }
}

@Composable
fun ProfileScreenContent(
    user: ProfileUser,
    onSettingUser: () -> Unit,
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
                    .padding(12.dp)


            ) {
                UserCard(
                    avatarUrl = user.avatar,
                    id = user.id,
                    email = user.email,
                    onClick = { onSettingUser() }
                )

                Button(
                    onClick = {
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                ) {
                    Text("Lưu thay đổi", color = Color.White)
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenContentPreview() {
    CapyVocab_FETheme {
        ProfileScreenContent(
            user = ProfileUser(
                id = 1,
                username = "",
                email = "ddd@gmail.com",
                avatar = null,
                status =  "hjj",
                streak = 3,
                lastStudyDate = null,
                totalStudyDay = 30,
            ),
            onSettingUser = { }
        )

    }
}

