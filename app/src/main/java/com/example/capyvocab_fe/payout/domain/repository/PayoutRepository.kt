package com.example.capyvocab_fe.payout.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.payout.data.model.PayoutRequest
import com.example.capyvocab_fe.payout.domain.model.Payout

interface PayoutRepository {
    suspend fun createPayout(request: PayoutRequest): Either<AppFailure, Unit>
    suspend fun getPayouts(
        page: Int = 1,
        limit: Int = 10,
        email: String? = null,
        username: String? = null,
        amount: Double? = null,
        status: String? = null,
        sort: Map<String, String> = mapOf("createdAt" to "DESC")
    ): Either<AppFailure, List<Payout>>
    suspend fun updatePayout(payoutId: Int, status: String): Either<AppFailure, Payout>
}