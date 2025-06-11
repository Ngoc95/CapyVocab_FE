package com.example.capyvocab_fe.payout.data.model

import com.example.capyvocab_fe.payout.domain.model.Payout

data class PayoutListResponse(
    val payouts: List<Payout>,
    val total: Int,
    val currentPage: Int,
    val totalPages: Int
)
