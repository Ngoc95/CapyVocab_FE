package com.example.capyvocab_fe.payout.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.auth.data.mapper.toAuthFailure
import com.example.capyvocab_fe.auth.domain.error.AuthFailure
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.payout.data.model.PayoutRequest
import com.example.capyvocab_fe.payout.data.model.UpdatePayoutRequest
import com.example.capyvocab_fe.payout.data.remote.PayoutApi
import com.example.capyvocab_fe.payout.domain.model.Payout
import com.example.capyvocab_fe.payout.domain.repository.PayoutRepository
import javax.inject.Inject

class PayoutRepositoryImpl @Inject constructor(
    private val payoutApi: PayoutApi
): PayoutRepository {
    override suspend fun getUserInfo(): Either<AuthFailure, User> {
        return Either.catch {
            val response = payoutApi.getUserInfo()
            response.metaData.user
        }.mapLeft {
            it.toAuthFailure()
        }
    }

    override suspend fun createPayout(request: PayoutRequest): Either<AppFailure, Unit> {
        return Either.catch {
            payoutApi.createPayout(request)
            Unit
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun getPayouts(
        page: Int,
        limit: Int,
        email: String?,
        username: String?,
        amount: Double?,
        status: String?,
    ): Either<AppFailure, List<Payout>> {
        return Either.catch {
            payoutApi.getPayouts(page, limit, email, username, amount, status).metaData.payouts
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun updatePayout(
        payoutId: Int,
        status: String
    ): Either<AppFailure, Payout> {
        return Either.catch {
            payoutApi.updatePayout(payoutId, UpdatePayoutRequest(status)).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }
}