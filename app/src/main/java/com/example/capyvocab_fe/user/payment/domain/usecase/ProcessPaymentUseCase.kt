package com.example.capyvocab_fe.user.payment.domain.usecase

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppError
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.user.payment.domain.model.Order
import com.example.capyvocab_fe.user.test.domain.model.Folder
import javax.inject.Inject

class ProcessPaymentUseCase @Inject constructor(
    private val createOrderUseCase: CreateOrderUseCase
) {
    suspend operator fun invoke(folder: Folder): Either<AppFailure, PaymentResult>  {
        return try {
            val result = createOrderUseCase(folder.id)
            when {
                folder.isFree -> {
                    result.fold(
                        ifLeft = { failure -> Either.Left(failure) },
                        ifRight = { orderResponse ->
                            Either.Right(PaymentResult.FreeContent(orderResponse.order))
                        }
                    )
                }

                else -> {
                    result.fold(
                        ifLeft = { failure -> Either.Left(failure) },
                        ifRight = { orderResponse ->
                            orderResponse.orderUrl?.let { url ->
                                Either.Right(PaymentResult.RequiresPayment(orderResponse.order, url))
                            } ?: Either.Left(
                                AppFailure(
                                    error = AppError.Unknown,
                                    message = "Payment url is missing"
                                )
                            )
                        }
                    )
                }
            }
        } catch (e: Exception) {
            Either.Left(e.toAppFailure())
        }
    }
}
sealed class PaymentResult {
    data class FreeContent(val order: Order) : PaymentResult()
    data class RequiresPayment(val order: Order, val paymentUrl: String) : PaymentResult()
}