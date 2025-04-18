package com.example.capyvocab_fe.admin.user.presentation.users_screen

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.admin.user.presentation.users_screen.components.UserCard
import com.example.capyvocab_fe.admin.user.presentation.users_screen.components.UserFormDialog
import com.example.capyvocab_fe.admin.user.presentation.util.components.FocusComponent
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors

@Composable
fun UserScreen(
    users: List<User>,
    onUserSave: (User) -> Unit,
    onUserDelete: (User) -> Unit,
    onUserExpandToggle: (User) -> Unit
) {
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
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
            }
        )
    }

    // Hiển thị dialog khi cần
    if (isDialogOpen) {
        FocusComponent {
            UserFormDialog(
                user = selectedUser,
                onDismiss = {
                    isDialogOpen = false
                    selectedUser = null
                },
                onSave = {
                    updatedUser ->
                    onUserSave(updatedUser)
                    isDialogOpen = false
                    selectedUser = null
                },
                onDelete = {
                    selectedUser?.let { onUserDelete(it) }
                    isDialogOpen = false
                    selectedUser = null
                }
            )
        }
    }
}

@Composable
fun UserScreenContent(
    users: List<User>,
    onUserExpandToggle: (User) -> Unit,
    onEditUser: (User) -> Unit,
    onAddUser: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 4.dp, start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Người dùng",
                fontSize = 33.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5E4A45)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(53.dp),
                tint = Color(0xFF5E4A45)
            )
        }

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
                colors = defaultTextFieldColors()
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
            items(users) { user ->
                var expanded by remember { mutableStateOf(false) }

                UserCard(
                    user = user,
                    isFree = user.roleId == 0,
                    isExpanded = expanded,
                    onExpandToggle = {
                        expanded = !expanded
                        onUserExpandToggle(user)
                    },
                    onEditClick = {
                        onEditUser(user)
                    }
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun UsersScreenPreview() {
//    val users = listOf(
//        User(
//            id = 1,
//            email = "alice@gmail.com",
//            username = "Alice",
//            password = "123456",
//            avatar = "https://randomuser.me/api/portraits/women/68.jpg",
//            status = 1,
//            streak = 10,
//            lastStudyDate = "12/04/2025",
//            totalStudyDay = 15,
//            totalLearnedCard = 120,
//            totalMasteredCard = 80,
//            roleId = 1
//        ),
//        User(
//            id = 2,
//            email = "bob@gmail.com",
//            username = "Bob",
//            password = "123456",
//            avatar = "https://randomuser.me/api/portraits/men/45.jpg",
//            status = 1,
//            streak = 20,
//            lastStudyDate = "14/04/2025",
//            totalStudyDay = 30,
//            totalLearnedCard = 200,
//            totalMasteredCard = 150,
//            roleId = 2
//        ),
//        User(
//            id = 3,
//            email = "carol@gmail.com",
//            username = "Carol",
//            password = "123456",
//            avatar = "https://randomuser.me/api/portraits/women/12.jpg",
//            status = 1,
//            streak = 5,
//            lastStudyDate = "09/04/2025",
//            totalStudyDay = 7,
//            totalLearnedCard = 50,
//            totalMasteredCard = 20,
//            roleId = 1
//        )
//    )
//    UserScreenContent(
//        users = users,
//        onUserExpandToggle = {},
//        onEditUser = {},
//        onAddUser = {}
//    )
//}