package com.example.capyvocab_fe.profile.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capyvocab_fe.core.data.model.RoleData
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.profile.domain.model.UserProfile
import com.example.capyvocab_fe.profile.presentation.components.AccountCard
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import kotlinx.coroutines.delay

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ProfileEvent.LoadProfile)
    }

    ProfileContent(
        user = state.user,
        isLoading = state.isLoading,
        errorMessage = state.errorMessage,
        onClickAccount = { navController.navigate(Route.ProfileSettingScreen.route) },
        onClickPayout = { viewModel.onEvent(ProfileEvent.Payout) },
        onClickLogout = {
            viewModel.onEvent(ProfileEvent.Logout)

            navController.navigate(Route.AuthNavigation.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    )
}


@Composable
fun ProfileContent(
    user: UserProfile?,
    isLoading: Boolean,
    errorMessage: String?,
    onClickAccount: () -> Unit,
    onClickPayout: () -> Unit,
    onClickLogout: () -> Unit
) {
    var visibleError by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Hiện snackbar 3 giây nếu có lỗi
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty()) {
            visibleError = errorMessage
            delay(3000)
            visibleError = ""
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            user?.let {
                AccountCard(
                    avatarUrl = it.avatar,
                    id = it.id,
                    email = it.email,
                    onClick = onClickAccount
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onClickPayout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Rút tiền")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text("Đăng xuất")
            }

            if (visibleError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = visibleError, color = Color.Red)
            }
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Xác nhận đăng xuất") },
                text = { Text("Bạn có chắc chắn muốn đăng xuất không?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            onClickLogout()
                        }
                    ) {
                        Text("Đăng xuất", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Hủy")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileContentPreview() {
    CapyVocab_FETheme {
        val roleDate = RoleData(
            id = 1,
            name = "User",
            description = ""
        )
        val fakeUser = UserProfile(
            id = 1,
            username = "Nguyen Van A",
            email = "nva@example.com",
            avatar = "N/A",
            totalStudyDay = 100,
            streak = 20,
            balance = 5000.0,
            status = "VERIFIED",
            lastStudyDate = null,
            role = roleDate
        )
        ProfileContent(
            user = fakeUser,
            isLoading = false,
            errorMessage = "Có lỗi xảy ra!",
            onClickAccount = {},
            onClickPayout = {},
            onClickLogout = {}
        )
    }
}
