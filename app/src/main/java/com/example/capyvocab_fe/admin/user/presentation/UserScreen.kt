package com.example.capyvocab_fe.admin.user.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.admin.user.presentation.components.UserCard
import com.example.capyvocab_fe.admin.user.presentation.components.UserFormDialog
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.ConfirmDeleteDialog
import com.example.capyvocab_fe.core.util.components.FocusComponent
import com.example.capyvocab_fe.navigation.Route
import kotlinx.coroutines.delay

@Composable
fun UserScreen(
    state: UserListState,
    navController: NavController,
    onEvent: (UserListEvent) -> Unit
) {
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    var userToDelete by remember { mutableStateOf<User?>(null) }
    var isDeleteConfirmDialogOpen by remember { mutableStateOf(false) }

    var visibleError by remember { mutableStateOf("") }

    val multiSelectTransition = if (state.isMultiSelecting) {
        remember { mutableStateOf(true) }
    } else {
        remember { mutableStateOf(false) }
    }
    LaunchedEffect(Unit) {
        onEvent(UserListEvent.LoadUsers)
    }

    //launchEffect to track transition to multi-select mode
    LaunchedEffect(state.isMultiSelecting) {
        multiSelectTransition.value = state.isMultiSelecting
    }

    BackHandler {
        if (state.isMultiSelecting) {
            onEvent(UserListEvent.CancelMultiSelect)
        } else {
            navController.navigate(Route.HomeScreen.route) {
                popUpTo(Route.HomeScreen.route) {
                    inclusive = false
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    // Khi errorMessage thay đổi, show snackbar trong 3 giây
    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            visibleError = state.errorMessage
            delay(3000) // hiện 3 giây
            visibleError = "" // ẩn sau 3 giây
        }
    }

    //Ko đóng dialog khi có lỗi
    LaunchedEffect(state.errorMessage, state.isLoading) {
        if (state.errorMessage.isEmpty() && !state.isLoading) {
            isDialogOpen = false
            selectedUser = null
        }
    }

    // Giao diện chính
    FocusComponent {
        UserScreenContent(
            users = state.users,
            selectedUsers = state.users.filter { state.selectedUsers.contains(it.id) },
            isMultiSelectMode = state.isMultiSelecting,
            onUserExpandToggle = { /* handle if needed */ },
            onEditUser = { user ->
                selectedUser = user
                isDialogOpen = true
            },
            onAddUser = {
                selectedUser = null
                isDialogOpen = true
            },
            isLoading = state.isLoading,
            isEndReached = state.isEndReached,
            onLoadMore = { onEvent(UserListEvent.LoadMoreUsers) },
            onUserLongPress = { user -> onEvent(UserListEvent.OnUserLongPress(user.id)) },
            onUserSelectToggle = { user -> onEvent(UserListEvent.OnUserSelectToggle(user.id)) },
        )
    }

    // Hiển thị dialog khi cần
    if (isDialogOpen) {
        UserFormDialog(
            user = selectedUser,
            errorMessage = visibleError,
            onDismiss = {
                isDialogOpen = false
                selectedUser = null
            },
            onSave = { updatedUser, password, confirmPassword, selectedImageUri ->
                onEvent(
                    UserListEvent.SaveUser(
                        updatedUser,
                        password,
                        confirmPassword,
                        selectedImageUri
                    )
                )
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

    //AlertDialog xác nhận trước khi xoá user
    if (isDeleteConfirmDialogOpen && userToDelete != null) {
        ConfirmDeleteDialog(
            message = "Bạn có chắc chắn muốn xoá người dùng \"${userToDelete?.username}\" không?",
            onConfirm = {
                onEvent(UserListEvent.DeleteUser(userToDelete!!.id))
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
    modifier: Modifier = Modifier,
    users: List<User>,
    selectedUsers: List<User>,
    isMultiSelectMode: Boolean,
    onUserExpandToggle: (User) -> Unit,
    onEditUser: (User) -> Unit,
    onAddUser: () -> Unit,
    isLoading: Boolean,
    isEndReached: Boolean,
    onLoadMore: () -> Unit,
    onUserLongPress: (User) -> Unit,
    onUserSelectToggle: (User) -> Unit,
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
            // Search bar & Add button - hide if in multi-select mode + animation
            AnimatedVisibility(
                visible = !isMultiSelectMode,
                enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                    animationSpec = tween(
                        300
                    ), initialOffsetX = { -it }),
                exit = fadeOut(animationSpec = tween(200)) + slideOutHorizontally(
                    animationSpec = tween(
                        200
                    ), targetOffsetX = { -it })
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 12.dp)
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
                    val isSelected = selectedUsers.contains(user)

                    //animation for selection
                    val cardElevation = animateDpAsState(
                        targetValue = if (isSelected && isMultiSelectMode) 12.dp else 8.dp,
                        label = "cardElevation"
                    )
                    val cardScale = animateFloatAsState(
                        targetValue = if (isSelected && isMultiSelectMode) 1.02f else 1f,
                        label = "cardScale"
                    )

                    Box(modifier = Modifier.scale(cardScale.value)) {
                        UserCard(
                            user = user,
                            isExpanded = expanded,
                            isMultiSelecting = isMultiSelectMode,
                            isSelected = selectedUsers.contains(user),
                            onExpandToggle = {
                                expanded = !expanded
                                onUserExpandToggle(user)
                            },
                            onEditClick = {
                                onEditUser(user)
                            },
                            onLongClick = {
                                onUserLongPress(user)
                            },
                            onCheckedChange = { isChecked ->
                                onUserSelectToggle(user)
                            },
                            cardElevation = cardElevation.value
                        )
                    }

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