package com.example.capyvocab_fe.user.profile.presentation

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.Black
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.profile.domain.model.ProfileUser
import kotlinx.coroutines.delay

@Composable
fun ProfileSettingScreenContent(
    user: ProfileUser,
    onBackClick:() -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
)
{
    BackHandler(enabled = true) {
        navController.navigate(Route.UserCoursesScreen.route) {
            popUpTo(Route.UserCoursesScreen.route) {
                inclusive = false
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val state by viewModel.state.collectAsState()
    var visibleError by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf(user.avatar) }

    // Khi errorMessage thay đổi, show  njm/snackbar trong 3 giây
    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            visibleError = state.errorMessage
            delay(3000) // hiện 3 giây
            visibleError = "" // ẩn sau 3 giây
        }
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            avatar = it.toString()
        }
    }

    ProfileSettingScreen(
        user = user,
        onBackClick = { onBackClick() },
        onSaveSettingChanges = {user ->
            viewModel.onEvent(ProfileEvent.UpdateUser(user))
        },
        onDeleteAccount = {

        },
        onLogout = {

        },
        avatar = avatar,
        selectAvatar = {
            launcher.launch("image/*")
        }

    )
}


@Composable
fun ProfileSettingScreen(
    user: ProfileUser,
    onLogout:() -> Unit,
    onSaveSettingChanges:(ProfileUser) -> Unit,
    onDeleteAccount:() -> Unit,
    onBackClick:() -> Unit,
    avatar: String?,
    selectAvatar:() -> Unit,
) {
    var username by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }
    var showSuccess by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 20.dp, start = 8.dp, bottom = 10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.backicon),
                modifier = Modifier.size(40.dp)
                    .clickable { onBackClick() },
                contentDescription = null,
            )
            Text(
                text = "Thông tin tài khoản",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Box(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.CenterHorizontally)
        )
        {
            AsyncImage(
                model = avatar, // your image resource
                contentDescription = "Avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Avatar",
                tint = Color.Blue,
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.White, CircleShape)
                    .padding(2.dp)
                    .align(Alignment.BottomEnd)
                    .clickable{ selectAvatar() }
            )
        }


        // ID
        Text(
            "ID: %05d".format(user.id.toInt()),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))
        // Email
        Text("Email", fontWeight = FontWeight.Bold)
        TextField(
            readOnly = true,
            value = email,
            onValueChange = {str -> email = str },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color(0xFF5E4A45),
                focusedIndicatorColor = Black,
                unfocusedIndicatorColor = Color.Black,
            ),
            shape = RoundedCornerShape(0.dp),
        )

        Text("UserName", fontWeight = FontWeight.Bold)
        TextField(
            value = username,
            onValueChange = {str -> username = str },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color(0xFF5E4A45),
                focusedIndicatorColor = Black,
                unfocusedIndicatorColor = Color.Black,
            ),
            shape = RoundedCornerShape(0.dp),
        )


        // Save Button
        Button(
            onClick = {
                val editUser = ProfileUser(
                    id = user.id,
                    username = username,
                    avatar = avatar,
                    email = email,
                    status = user.status,
                    lastStudyDate = user.lastStudyDate,
                    streak = user.streak,
                    totalStudyDay = user.totalStudyDay,
                )
                onSaveSettingChanges(editUser)
                showSuccess = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
        ) {
            Text("Lưu thay đổi", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout
        TextButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Đăng xuất", color = Color.Red)
        }

        // Delete Account
        TextButton(
            onClick = onDeleteAccount,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xóa tài khoản", color = Color.Red, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Success message
        if (showSuccess) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .background(Color(0xFF4CAF50), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth()
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cập nhật thành công", color = Color.White)
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingPreview() {
    CapyVocab_FETheme {

        ProfileSettingScreen (
            user = ProfileUser(
                id = 1,
                username = "fd",
                email = "ddd",
                avatar = "dd",
                streak = 1,
                status = "VERIFIED",
                lastStudyDate = null,
                totalStudyDay = 3,
            ),
            onLogout = {},
            onSaveSettingChanges = { },
            onDeleteAccount = { },
            onBackClick = { },
            selectAvatar = { },
            avatar = ""

        )

    }
}