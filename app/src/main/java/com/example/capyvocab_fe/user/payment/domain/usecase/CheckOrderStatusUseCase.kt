package com.example.capyvocab_fe.user.payment.domain.usecase

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.user.payment.domain.model.Order
import com.example.capyvocab_fe.user.payment.domain.repository.PaymentRepository
import javax.inject.Inject

class CheckOrderStatusUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(folderId: Int): Either<AppFailure, Order?> {
        return paymentRepository.checkOrderStatus(folderId)
    }
}