package com.example.capyvocab_fe.auth.data.remote.model

import com.example.capyvocab_fe.auth.domain.model.User

data class GetAccountResponse(
    val user: User
)