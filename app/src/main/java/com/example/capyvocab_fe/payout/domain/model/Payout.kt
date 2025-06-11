package com.example.capyvocab_fe.payout.domain.model

import com.example.capyvocab_fe.auth.domain.model.User

data class Payout(
    val id: Int,
    val amount: Double,
    val createdAt: String,
    val createdBy: User,
    val nameBank: String,
    val numberAccount: String,
    val status: String
)