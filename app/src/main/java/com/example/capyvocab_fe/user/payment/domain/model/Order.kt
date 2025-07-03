package com.example.capyvocab_fe.user.payment.domain.model

import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.user.test.domain.model.Folder

data class Order(
    val id: String,
    val amount: Double,
    val status: OrderStatus,
    val createdAt: String,
    val bankTranNo: String? = null,
    val nameBank: String? = null,
    val payDate: String? = null,
    val folder: Folder,
    val createdBy: User
)

enum class OrderStatus {
    PENDING, SUCCESS, FAILED, CANCELLED
}