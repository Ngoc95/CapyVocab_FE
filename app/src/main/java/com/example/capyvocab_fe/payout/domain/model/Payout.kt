package com.example.capyvocab_fe.payout.domain.model

import com.example.capyvocab_fe.auth.domain.model.User
import java.util.Date

enum class PayoutStatus {
    SUCCESS,
    FAILED,
    PENDING
}

fun PayoutStatus.toDisplayName(): String {
    return when (this) {
        PayoutStatus.SUCCESS -> "Thành công"
        PayoutStatus.FAILED -> "Từ chối"
        PayoutStatus.PENDING -> "Đang chờ"
    }
}

data class Payout(
    val id: Int,
    val amount: Double,
    val createdAt: Date? = null,
    val createdBy: User,
    val nameBank: String,
    val numberAccount: String,
    val status: PayoutStatus
)