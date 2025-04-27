package com.example.capyvocab_fe.admin.user.presentation

import android.net.Uri
import com.example.capyvocab_fe.admin.user.domain.model.User

data class UserListState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val selectedUsers: Set<Int> = emptySet(),
    val isMultiSelecting: Boolean = false,
    val isSelectAll: Boolean = false,
    val errorMessage: String = "",
    val selectedAvatarUri: Uri? = null,
    val currentPage: Int = 1,
    val isEndReached: Boolean = false
)