package com.example.capyvocab_fe.payout.presentation

sealed class PayoutEvent {
    object CreatePayout : PayoutEvent()
    data class AmountChanged(val amount: String) : PayoutEvent()
    data class NumberAccountChanged(val numberAccount: String) : PayoutEvent()
    data class NameBankChanged(val nameBank: String) : PayoutEvent()
    object LoadPayouts : PayoutEvent()
    object LoadMorePayouts : PayoutEvent()

    data class UpdatePayout(val payoutId: Int, val status: String) : PayoutEvent()
}