package com.example.capyvocab_fe.admin.user.presentation.users_screen

import com.example.capyvocab_fe.admin.user.domain.model.User

data class UserListState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val errorMessage: String = ""
)