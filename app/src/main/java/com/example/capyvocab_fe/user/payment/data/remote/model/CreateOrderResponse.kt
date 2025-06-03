package com.example.capyvocab_fe.user.payment.data.remote.model

import com.example.capyvocab_fe.user.payment.domain.model.Order

data class CreateOrderResponse(
    val order: Order,
    val orderUrl: String?
)