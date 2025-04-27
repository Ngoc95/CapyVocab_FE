package com.example.capyvocab_fe.admin.user.presentation

import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.admin.user.presentation.components.UserCard
import com.example.capyvocab_fe.admin.user.presentation.components.UserFormDialog
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.ConfirmDeleteDialog
import com.example.capyvocab_fe.core.ui.components.TopBarTitle
import com.example.capyvocab_fe.core.util.components.FocusComponent
import kotlinx.coroutines.delay

@Composable
fun UserScreen(
    users: List<User>,
    errorMessage: String,
    isLoading: Boolean,
    isEndReached: Boolean,
    onUserSave: (User, String?, String?, Uri?) -> Unit,
    onUserDelete: (User) -> Unit,
    onUserExpandToggle: (User) -> Unit,
    onLoadMore: () -> Unit
) {
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    var userToDelete by remember { mutableStateOf<User?>(null) }
    var isDeleteConfirmDialogOpen by remember { mutableStateOf(false) }

    var visibleError by remember { mutableStateOf("") }

    // Khi errorMessage thay đổi, show snackbar trong 3 giây
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            visibleError = errorMessage
            delay(3000) // hiện 3 giây
            visibleError = "" // ẩn sau 3 giây
        }
    }

    //Ko đóng dialog khi có lỗi
    LaunchedEffect(errorMessage, isLoading) {
        if (errorMessage.isEmpty() && !isLoading) {
            isDialogOpen = false
            selectedUser = null
        }
    }

    // Giao diện chính
    FocusComponent {
        UserScreenContent(
            users = users,
            onUserExpandToggle = onUserExpandToggle,
            onEditUser = { user ->
                selectedUser = user
                isDialogOpen = true
            },
            onAddUser = {
                selectedUser = null
                isDialogOpen = true
            },
            isLoading = isLoading,
            isEndReached = isEndReached,
            onLoadMore = onLoadMore
        )
    }

    // Hiển thị dialog khi cần
    if (isDialogOpen) {
        FocusComponent {
            UserFormDialog(
                user = selectedUser,
                errorMessage = visibleError,
                onDismiss = {
                    isDialogOpen = false
                    selectedUser = null
                },
                onSave = { updatedUser, password, confirmPassword, selectedImageUri ->
                    onUserSave(updatedUser, password, confirmPassword, selectedImageUri)
                },
                onDelete = {
                    selectedUser?.let {
                        userToDelete = it
                        isDeleteConfirmDialogOpen = true
                    }
                    isDialogOpen = false
                    selectedUser = null
                }
            )
        }
    }

    //AlertDialog xác nhận trước khi xoá user
    if (isDeleteConfirmDialogOpen && userToDelete != null) {
        ConfirmDeleteDialog(
            message = "Bạn có chắc chắn muốn xoá người dùng \"${userToDelete?.username}\" không?",
            onConfirm = {
                onUserDelete(userToDelete!!)
                isDeleteConfirmDialogOpen = false
                userToDelete = null
            },
            onDismiss = {
                isDeleteConfirmDialogOpen = false
                userToDelete = null
            }
        )
    }

}

@Composable
fun UserScreenContent(
    users: List<User>,
    onUserExpandToggle: (User) -> Unit,
    onEditUser: (User) -> Unit,
    onAddUser: () -> Unit,
    isLoading: Boolean,
    isEndReached: Boolean,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // Detect khi cuộn đến gần cuối
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()?.index ?: 0
                val totalItems = listState.layoutInfo.totalItemsCount

                if (lastVisibleItem >= totalItems - 2 && !isLoading && !isEndReached) {
                    onLoadMore()
                }
            }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier.fillMaxSize()) {
            // Top bar
            TopBarTitle("Người dùng")

            // Search bar & Add button
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var searchQuery by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Tìm người dùng") },
                    shape = RoundedCornerShape(30.dp),
                    singleLine = true,
                    trailingIcon = {
                        Box(
                            modifier = Modifier
                                .size(width = 39.dp, height = 36.dp)
                                .background(
                                    color = Color(0xFF00D9FF),
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    colors = defaultTextFieldColors(),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Image(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null,
                    modifier = Modifier
                        .size(55.dp)
                        .clickable(onClick = onAddUser)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // User list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(users) { index, user ->
                    var expanded by remember { mutableStateOf(false) }

                    UserCard(
                        user = user,
                        isExpanded = expanded,
                        onExpandToggle = {
                            expanded = !expanded
                            onUserExpandToggle(user)
                        },
                        onEditClick = {
                            onEditUser(user)
                        }
                    )

                    // Load thêm nếu gần cuối
                    if (index >= users.size - 3 && !isLoading && !isEndReached) {
                        onLoadMore()
                    }
                }

                // loading indicator khi đang load thêm
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsersScreenPreview() {
    val users = listOf(
        User(
            id = 1,
            email = "alice@gmail.com",
            username = "Alice",
            avatar = "https://randomuser.me/api/portraits/women/68.jpg",
            status = "VERIFIED",
            streak = 10,
            lastStudyDate = "12/04/2025",
            totalStudyDay = 15,
            totalLearnedCard = 120,
            totalMasteredCard = 80,
            roleId = 1
        ),
        User(
            id = 2,
            email = "bob@gmail.com",
            username = "Bob",
            avatar = "https://randomuser.me/api/portraits/men/45.jpg",
            status = "NOT_VERIFIED",
            streak = 20,
            lastStudyDate = "14/04/2025",
            totalStudyDay = 30,
            totalLearnedCard = 200,
            totalMasteredCard = 150,
            roleId = 2
        ),
        User(
            id = 3,
            email = "carol@gmail.com",
            username = "Carol",
            avatar = "https://randomuser.me/api/portraits/women/12.jpg",
            status = "VERIFIED",
            streak = 5,
            lastStudyDate = "09/04/2025",
            totalStudyDay = 7,
            totalLearnedCard = 50,
            totalMasteredCard = 20,
            roleId = 1
        )
    )
    UserScreenContent(
        users = users,
        onUserExpandToggle = {},
        onEditUser = {},
        onAddUser = {},
        isLoading = false,
        isEndReached = true,
        onLoadMore = {}
    )
}