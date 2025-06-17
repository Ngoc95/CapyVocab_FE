package com.example.capyvocab_fe.payout.presentation

import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.payout.domain.model.Payout

data class PayoutState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = "",
    val amount: String = "",
    val numberAccount: String = "",
    val nameBank: String = "",
    val payouts: List<Payout> = emptyList(),
    val currentPage: Int = 1,
    val isEndReached: Boolean = false,
    val currentUser: User? = null
)