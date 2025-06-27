package com.example.capyvocab_fe.user.profile.presentation

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.core.ui.components.TopBarTitle
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.White
import com.example.capyvocab_fe.ui.theme.dimens
import com.example.capyvocab_fe.user.profile.domain.model.ProfileUser
import com.example.capyvocab_fe.user.profile.presentation.Components.UserCard
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onSettingUser:(ProfileUser) -> Unit,
    onChangePassword: () -> Unit,
    onPayoutClick: () -> Unit
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
            onPayoutClick = onPayoutClick,
            onChangePassword = onChangePassword

        )
    }
}

@Composable
fun ProfileScreenContent(
    user: ProfileUser,
    onSettingUser: () -> Unit,
    onPayoutClick: () -> Unit,
    onChangePassword: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(White)
    ) {
        Column {
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
//
//                Button(
//                    onClick = {
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
//                ) {
//                    Text("Lưu thay đổi", color = Color.White)
//                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)) // Bóng đổ
                    .background(color = Color(0xFFF1F9FF), shape = RoundedCornerShape(12.dp)) // Nền xanh nhạt
                    .clickable { onChangePassword() }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.change_pass), // Đổi icon nếu cần
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Đổi mật khẩu",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)) // Bóng đổ
                    .background(color = Color(0xFFF1F9FF), shape = RoundedCornerShape(12.dp)) // Nền xanh nhạt
                    .clickable { onPayoutClick() }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_payout), // Đổi icon nếu cần
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Rút tiền",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenContentPreview() {
    //CapyVocab_FETheme {
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
            onSettingUser = { },
            onPayoutClick = { },
            onChangePassword = { }
        )

    //}
}

