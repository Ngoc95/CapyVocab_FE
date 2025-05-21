package com.example.capyvocab_fe.user.test.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.White
import com.example.capyvocab_fe.user.test.domain.model.Folder
import com.example.capyvocab_fe.user.test.presentation.screens.screen_contents.CreateTestContent
import com.example.capyvocab_fe.user.test.presentation.screens.screen_contents.CreatedTestsContent
import com.example.capyvocab_fe.user.test.presentation.screens.screen_contents.DoTestContent
import com.example.capyvocab_fe.user.test.presentation.screens.screen_contents.EnterCodeContent
import com.example.capyvocab_fe.user.test.presentation.screens.screen_contents.TestDetailContent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseEvent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseViewModel
import kotlinx.coroutines.delay

@Composable
fun TestScreen(
    viewModel: ExerciseViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ExerciseEvent.GetAllFolders())
    }

    TestScreenContent(
        folders = state.folders,
        user = state.currentUser,
        navController = navController,
        viewModel = viewModel
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TestScreenContent(
    folders: List<Folder>,
    user: User?,
    navController: NavController,
    viewModel: ExerciseViewModel
) {
    val tabs = listOf("Làm test", "Nhập code", "Đã tạo", "Tạo mới")
    var selectedFolder by remember { mutableStateOf<Folder?>(null) }
    val state = viewModel.state.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .statusBarsPadding()
    ) {
        // Header với thông tin người dùng
        UserInfoHeader(
            user = user
        )

        // Thanh điều hướng chức năng
        TabRow(
            selectedTabIndex = state.currentTab,
            containerColor = Color.White
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = state.currentTab == index,
                    onClick = {
                        // Gửi sự kiện navigation tương ứng
                        when (index) {
                            0 -> viewModel.onEvent(ExerciseEvent.NavigateToDoTest)
                            1 -> viewModel.onEvent(ExerciseEvent.NavigateToEnterCode)
                            2 -> viewModel.onEvent(ExerciseEvent.NavigateToCreatedTests)
                            3 -> viewModel.onEvent(ExerciseEvent.NavigateToCreateTest)
                        }
                        // Reset folder đã chọn khi chuyển tab
                        if (state.currentTab != index) {
                            selectedFolder = null
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp),
                    content = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Biểu tượng cho từng tab
                            Icon(
                                painter = when (index) {
                                    0 -> painterResource(id = R.drawable.user_test_do)
                                    1 -> painterResource(id = R.drawable.user_test_code)
                                    2 -> painterResource(id = R.drawable.user_test_created)
                                    else -> painterResource(id = R.drawable.user_test_create)
                                },
                                contentDescription = title,
                                modifier = Modifier.size(30.dp),
                                tint = Color.Black
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Văn bản dưới biểu tượng
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                color = if (state.currentTab == index) Color(0xFF42B3FF) else Color.Black
                            )
                        }
                    }
                )
            }
        }

        // Hiển thị thông báo thành công nếu có
        state.successMessage?.let { message ->
            Text(
                text = message,
                color = Color(0xFF4CAF50),
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Xóa thông báo sau 3 giây
            LaunchedEffect(message) {
                delay(3000)
                viewModel.clearSuccessMessage()
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Phần nội dung - mặc định là Làm test
        Box(modifier = Modifier.fillMaxHeight()) {
            when (state.currentTab) {
                0 -> {
                    // Nếu đã chọn folder thì hiển thị TestDetailContent, ngược lại hiển thị danh sách folder
                    if (selectedFolder != null) {
                        TestDetailContent(
                            folder = selectedFolder!!,
                            onBack = {
                                selectedFolder = null
                                viewModel.onEvent(ExerciseEvent.GetAllFolders())
                            },
                            onVoteClick = { folderId ->
                                viewModel.onEvent(ExerciseEvent.VoteFolder(folderId))
                            },
                            onUnVoteClick = { folderId ->
                                viewModel.onEvent(ExerciseEvent.UnvoteFolder(folderId))
                            },
                            navController = navController
                        )
                    } else {
                        DoTestContent(
                            folders = folders,
                            onFolderClick = { folder ->
                                selectedFolder = folder
                            },
                            viewModel = viewModel
                        )
                    }
                }

                1 -> EnterCodeContent(
                    viewModel = viewModel,
                    onFolderFound = { folder ->
                        selectedFolder = folder
                        viewModel.onEvent(ExerciseEvent.NavigateToDoTest) // Chuyển sang tab Làm test
                    }
                )

                2 -> CreatedTestsContent(
                    viewModel = viewModel,
                    onFolderClick = { folder ->
                        selectedFolder = folder
                        viewModel.onEvent(ExerciseEvent.NavigateToDoTest) // Chuyển sang tab Làm test
                    }
                )

                3 -> CreateTestContent(
                    viewModel = viewModel,
                    onFolderCreated = { folder ->
                        // Chuyển đến tab Đã tạo thay vì chi tiết folder
                        viewModel.onEvent(ExerciseEvent.NavigateToCreatedTests)
                    }
                )
            }
        }
    }
}

@Composable
fun UserInfoHeader(
    user: User?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo và tên ứng dụng
        Text(
            text = "CapyVocab",
            color = Color(0xFF42B3FF),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        // Icon thông báo
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }

    // Thông tin người dùng
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB5EEFF))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ảnh đại diện
        AsyncImage(
            model = user?.avatar,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.default_avt),
            error = painterResource(R.drawable.default_avt),
            fallback = painterResource(R.drawable.default_avt)
        )
        // Thông tin ID và mô tả
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            InfoRow("ID", user?.username.toString())
            InfoRow("Đã tham gia", "10 bài test")
        }
    }
}

@Composable
fun InfoRow(label: String, text: String) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
            ) {
                append("$label: ")
            }
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            ) {
                append(text)
            }
        }
    )
}

@Preview
@Composable
private fun TestScreenPreview() {
    CapyVocab_FETheme {
        TestScreen(navController = rememberNavController())
    }
}