package com.example.capyvocab_fe.user.payment.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.user.payment.data.remote.model.CreateOrderResponse
import com.example.capyvocab_fe.user.payment.domain.model.Order

interface PaymentRepository {
    suspend fun createOrder(folderId: Int): Either<AppFailure, CreateOrderResponse>
    suspend fun handleVnpayReturn(params: Map<String, String>): Either<AppFailure, Order>
}