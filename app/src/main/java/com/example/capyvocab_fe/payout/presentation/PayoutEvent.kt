package com.example.capyvocab_fe.payout.presentation

import androidx.compose.ui.text.input.TextFieldValue
import com.example.capyvocab_fe.payout.domain.model.PayoutStatus

sealed class PayoutEvent {
    object CreatePayout : PayoutEvent()
    data class AmountChanged(val amount: TextFieldValue) : PayoutEvent()
    data class NumberAccountChanged(val numberAccount: String) : PayoutEvent()
    data class NameBankChanged(val nameBank: String) : PayoutEvent()
    object LoadPayouts : PayoutEvent()
    object LoadMorePayouts : PayoutEvent()

    data class UpdatePayout(val payoutId: Int, val status: String) : PayoutEvent()
    data class PayoutStatusChanged(val status: PayoutStatus) : PayoutEvent()
}