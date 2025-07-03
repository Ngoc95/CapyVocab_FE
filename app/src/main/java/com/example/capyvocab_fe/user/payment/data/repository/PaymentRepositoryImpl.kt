package com.example.capyvocab_fe.user.payment.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.user.payment.data.remote.PaymentApi
import com.example.capyvocab_fe.user.payment.data.remote.model.CreateOrderResponse
import com.example.capyvocab_fe.user.payment.domain.model.Order
import com.example.capyvocab_fe.user.payment.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApi
): PaymentRepository {
    override suspend fun createOrder(folderId: Int): Either<AppFailure, CreateOrderResponse> {
        return Either.catch {
            api.createOrder(folderId).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun handleVnpayReturn(params: Map<String, String>): Either<AppFailure, Order> {
        return Either.catch {
            api.handleVnpayReturn(params).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun checkOrderStatus(folderId: Int): Either<AppFailure, Order?> {
        return Either.catch {
            api.checkOrderStatus(folderId).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun cancelOrder(orderId: String): Either<AppFailure, Unit> {
        return Either.catch {
            api.cancelOrder(orderId)
            Unit
        }.mapLeft {
            it.toAppFailure()
        }
    }
}