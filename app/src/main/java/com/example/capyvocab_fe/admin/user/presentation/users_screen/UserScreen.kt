package com.example.capyvocab_fe.admin.user.presentation.users_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.admin.user.presentation.users_screen.components.User
import com.example.capyvocab_fe.admin.user.presentation.users_screen.components.UserCard

@Composable
fun UserScreen(
    users: List<User>,
    onUserExpandToggle: (User) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            UserCard(
                user = user,
                isFree =  true,
                isExpanded = false,
                onExpandToggle = { onUserExpandToggle(user) }
            )
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
            password = "123456",
            avatar = "https://randomuser.me/api/portraits/women/68.jpg",
            status = 1,
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
            password = "123456",
            avatar = "https://randomuser.me/api/portraits/men/45.jpg",
            status = 1,
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
            password = "123456",
            avatar = "https://randomuser.me/api/portraits/women/12.jpg",
            status = 1,
            streak = 5,
            lastStudyDate = "09/04/2025",
            totalStudyDay = 7,
            totalLearnedCard = 50,
            totalMasteredCard = 20,
            roleId = 1
        )
    )
    UserScreen(users = users)
}