package com.example.capyvocab_fe.payout.presentation

import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.payout.domain.model.Payout
import androidx.compose.ui.text.input.TextFieldValue
import com.example.capyvocab_fe.payout.domain.model.PayoutStatus

data class PayoutState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = "",
    val amount: TextFieldValue = TextFieldValue(""),
    val numberAccount: String = "",
    val nameBank: String = "",
    val payouts: List<Payout> = emptyList(),
    val currentPage: Int = 1,
    val isEndReached: Boolean = false,
    val currentUser: User? = null,
    val selectedStatus: PayoutStatus = PayoutStatus.PENDING
)