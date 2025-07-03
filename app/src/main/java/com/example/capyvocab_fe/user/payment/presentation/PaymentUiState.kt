package com.example.capyvocab_fe.user.payment.presentation

import com.example.capyvocab_fe.user.payment.domain.model.Order
import com.example.capyvocab_fe.user.test.domain.model.Folder

data class PaymentUiState(
    val isLoading: Boolean = false,
    val isProcessingPayment: Boolean = false,
    val isCheckingOrderStatus: Boolean = false,
    val showPaymentDialog: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val paymentUrl: String? = null,
    val error: String? = null,
    val folder: Folder? = null,
    val order: Order? = null,
    val existingOrder: Order? = null
)
