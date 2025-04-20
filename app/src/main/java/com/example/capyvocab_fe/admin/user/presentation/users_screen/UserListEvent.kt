package com.example.capyvocab_fe.admin.user.presentation.users_screen

import com.example.capyvocab_fe.admin.user.domain.model.User

sealed class UserListEvent {
    object LoadUsers : UserListEvent()
    data class SaveUser(val user: User, val password: String?, val confirmPassword: String?) : UserListEvent()
}
