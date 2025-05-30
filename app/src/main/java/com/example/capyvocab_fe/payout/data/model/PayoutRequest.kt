package com.example.capyvocab_fe.payout.data.model

data class PayoutRequest(
    val amount: Double?,
    val numberAccount: String?,
    val nameBank: String?
)