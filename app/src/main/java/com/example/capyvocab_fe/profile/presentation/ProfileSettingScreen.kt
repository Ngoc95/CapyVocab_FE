package com.example.capyvocab_fe.profile.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.core.data.model.RoleData
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.profile.domain.model.UserProfile
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

@Composable
fun ProfileSettingScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val user = state.user ?: return

    ProfileSettingContent(
        user = user,
        isLoading = state.isLoading,
        errorMessage = state.errorMessage,
        onBack = { navController.popBackStack() },
        onUpdate = { avatar, username, email ->
            viewModel.onEvent(
                ProfileEvent.UpdateProfile(avatar, email, username)
            )
        },
        onChangePassword = {
            navController.navigate(Route.ChangePasswordScreen.route)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileSettingContent() {
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

        ProfileSettingContent(
            user = fakeUser,
            isLoading = false,
            errorMessage = null,
            onBack = {},
            onUpdate = { _, _, _ -> },
            onChangePassword = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingContent(
    user: UserProfile,
    isLoading: Boolean,
    errorMessage: String?,
    onBack: () -> Unit,
    onUpdate: (avatar: Any?, username: String, email: String) -> Unit,
    onChangePassword: () -> Unit
) {
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var username by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông tin tài khoản") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AvatarPicker(
                    avatarUrl = if (user.avatar == "N/A") null else user.avatar,
                    onAvatarSelected = { avatarUri = it }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            InfoRow(label = "ID:", value = user.id.toString())
            Spacer(modifier = Modifier.height(6.dp))

            InfoRow(label = "Tổng ngày học:", value = "${user.totalStudyDay} ngày")
            Spacer(modifier = Modifier.height(10.dp))

            InfoRow(label = "Chuỗi:", value = "${user.streak} ngày")
            Spacer(modifier = Modifier.height(10.dp))

            InfoRow(label = "Số dư:", value = "${user.balance} VNĐ")

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Tên người dùng") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onUpdate(avatarUri ?: user.avatar, username, email)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lưu thay đổi")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onChangePassword,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Đổi mật khẩu")
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            errorMessage?.let {
                Text(text = it, color = Color.Red)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = Color.Gray
        )
    }
}

@Composable
fun AvatarPicker(
    avatarUrl: String?,
    onAvatarSelected: (Uri?) -> Unit
) {
    var avatarUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        avatarUri = uri
        onAvatarSelected(uri)
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .size(100.dp)
            .clickable { launcher.launch("image/*") }
    ) {
        // Nền avatar được clip tròn
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            when {
                avatarUri != null -> {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                !avatarUrl.isNullOrEmpty() -> {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    Image(
                        painter = painterResource(id = R.drawable.default_avt),
                        contentDescription = "Default Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Icon edit
        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(24.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.Blue,
                modifier = Modifier.size(16.dp)
            )
        }
    }

}

