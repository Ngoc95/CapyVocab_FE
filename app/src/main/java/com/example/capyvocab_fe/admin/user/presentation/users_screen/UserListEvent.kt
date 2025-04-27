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

    data class OnUserLongPress(val userId: Int) : UserListEvent()
    data class OnUserSelectToggle(val userId: Int) : UserListEvent()
    object OnSelectAllToggle : UserListEvent()
    object OnDeleteSelectedUsers : UserListEvent()
    object CancelMultiSelect : UserListEvent()
}
