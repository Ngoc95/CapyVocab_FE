package com.example.capyvocab_fe.user.profile.presentation

import android.provider.ContactsContract
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.learn.presentation.LearnEvent
import com.example.capyvocab_fe.user.profile.domain.model.ProfileUser


sealed class ProfileEvent {
    object GetCurrentUser : ProfileEvent()
    object ClearError : ProfileEvent()
    object DeleteUser : ProfileEvent()
    object Logout : ProfileEvent()
    data class UpdateUser(val user: ProfileUser) : ProfileEvent()
    data class EmailChange(val Email: String) : ProfileEvent()
    data class PasswordChange(val NewPass: String) : ProfileEvent()

}