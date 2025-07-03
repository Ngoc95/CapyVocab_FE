package com.example.capyvocab_fe.user.payment.presentation

import com.example.capyvocab_fe.user.test.domain.model.Folder

sealed class PaymentUiEvent {
    data class LoadFolder(val folder: Folder) : PaymentUiEvent()
    object InitiatePayment : PaymentUiEvent()
    object ConfirmPayment : PaymentUiEvent()
    object CancelPayment : PaymentUiEvent()
    data class PaymentCompleted(val returnParams: Map<String, String>) : PaymentUiEvent()
    object DismissError : PaymentUiEvent()
    object DismissSuccess : PaymentUiEvent()
    object ClearPaymentUrl : PaymentUiEvent()
    data class CheckOrderStatus(val folderId: Int) : PaymentUiEvent()
    data class CancelOrder(val orderId: String) : PaymentUiEvent()
}
