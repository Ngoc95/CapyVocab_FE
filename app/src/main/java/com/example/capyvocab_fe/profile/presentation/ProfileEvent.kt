package com.example.capyvocab_fe.profile.presentation

sealed class ProfileEvent{
    object LoadProfile : ProfileEvent()
    object Logout : ProfileEvent()
    object DeleteAccount : ProfileEvent()
    data class UpdateProfile(
        val avatar: Any?,
        val email: String,
        val username: String
    ) : ProfileEvent()

    data class ChangePassword(
        val oldPassword: String,
        val newPassword: String
    ) : ProfileEvent()
}
