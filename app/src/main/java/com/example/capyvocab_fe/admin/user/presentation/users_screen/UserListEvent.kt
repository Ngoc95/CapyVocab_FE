package com.example.capyvocab_fe.admin.user.presentation.users_screen

import android.net.Uri
import com.example.capyvocab_fe.admin.user.domain.model.User

sealed class UserListEvent {
    object LoadUsers : UserListEvent()
    object LoadMoreUsers : UserListEvent()
    data class SaveUser(
        val user: User,
        val password: String?,
        val confirmPassword: String?,
        val avatarUri: Uri? = null
    ) : UserListEvent()
    data class DeleteUser(val userId: Int) : UserListEvent()
}
