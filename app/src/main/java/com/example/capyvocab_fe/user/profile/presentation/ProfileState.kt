package com.example.capyvocab_fe.user.profile.presentation

import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.profile.domain.model.ProfileUser

data class ProfileState (
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val currentUser: ProfileUser? = null,
)