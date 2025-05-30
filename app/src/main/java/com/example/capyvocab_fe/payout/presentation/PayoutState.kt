package com.example.capyvocab_fe.payout.presentation

import com.example.capyvocab_fe.payout.domain.model.Payout

data class PayoutState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false,
    val amount: Double = 0.0,
    val numberAccount: String = "",
    val nameBank: String = "",
    val payouts: List<Payout> = emptyList(),
    val currentPage: Int = 1,
    val isEndReached: Boolean = false
)