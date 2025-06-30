package com.example.capyvocab_fe.profile.presentation

import com.example.capyvocab_fe.profile.domain.model.UserProfile

data class ProfileState (
    val user: UserProfile? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
