package com.example.capyvocab_fe.user.payment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.capyvocab_fe.user.payment.domain.usecase.PaymentResult
import com.example.capyvocab_fe.user.payment.domain.usecase.ProcessPaymentUseCase
import com.example.capyvocab_fe.user.payment.domain.usecase.VerifyPaymentUseCase
import com.example.capyvocab_fe.user.payment.domain.usecase.CheckOrderStatusUseCase
import com.example.capyvocab_fe.user.payment.domain.usecase.CancelOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val processPaymentUseCase: ProcessPaymentUseCase,
    private val verifyPaymentUseCase: VerifyPaymentUseCase,
    private val checkOrderStatusUseCase: CheckOrderStatusUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState

    fun onEvent(event: PaymentUiEvent) {
        when (event) {
            is PaymentUiEvent.LoadFolder -> {
                _uiState.value = _uiState.value.copy(folder = event.folder)
            }

            is PaymentUiEvent.InitiatePayment -> {
                _uiState.value = _uiState.value.copy(showPaymentDialog = true)
            }

            is PaymentUiEvent.ConfirmPayment -> {
                processPayment()
            }

            is PaymentUiEvent.CancelPayment -> {
                _uiState.value = _uiState.value.copy(showPaymentDialog = false)
            }

            is PaymentUiEvent.PaymentCompleted -> {
                verifyPayment(event.returnParams)
            }

            is PaymentUiEvent.DismissError -> {
                _uiState.value = _uiState.value.copy(error = null)
            }

            is PaymentUiEvent.DismissSuccess -> {
                _uiState.value = _uiState.value.copy(showSuccessDialog = false)
            }
            
            is PaymentUiEvent.ClearPaymentUrl -> {
                _uiState.update { it.copy(paymentUrl = null) }
            }

            is PaymentUiEvent.CheckOrderStatus -> {
                checkOrderStatus(event.folderId)
            }

            is PaymentUiEvent.CancelOrder -> {
                cancelOrder(event.orderId)
            }
        }
    }

    private fun processPayment() {
        val folder = _uiState.value.folder ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                showPaymentDialog = false,
                error = null
            )

            when (val result = processPaymentUseCase(folder)) {
                is Either.Right -> {
                    when (val paymentResult = result.value) {
                        is PaymentResult.FreeContent -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                order = paymentResult.order,
                                showSuccessDialog = true
                            )
                        }
                        is PaymentResult.RequiresPayment -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                order = paymentResult.order,
                                paymentUrl = paymentResult.paymentUrl
                            )
                        }
                    }
                }

                is Either.Left -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.value.message ?: result.value.error.defaultMessage
                    )
                }
            }
        }
    }

    private fun verifyPayment(returnParams: Map<String, String>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isProcessingPayment = true
            )

            when (val result = verifyPaymentUseCase(returnParams)) {
                is Either.Right -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isProcessingPayment = false,
                        order = result.value,
                        paymentUrl = null,
                        showSuccessDialog = true
                    )
                }

                is Either.Left -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isProcessingPayment = false,
                        paymentUrl = null,
                        error = result.value.message ?: result.value.error.defaultMessage
                    )
                }
            }
        }
    }

    private fun checkOrderStatus(folderId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCheckingOrderStatus = true,
                error = null
            )

            when (val result = checkOrderStatusUseCase(folderId)) {
                is Either.Right -> {
                    _uiState.value = _uiState.value.copy(
                        isCheckingOrderStatus = false,
                        existingOrder = result.value
                    )

                }

                is Either.Left -> {
                    _uiState.value = _uiState.value.copy(
                        isCheckingOrderStatus = false,
                        error = result.value.message ?: result.value.error.defaultMessage
                    )
                }
            }
        }
    }

    private fun cancelOrder(orderId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = cancelOrderUseCase(orderId)) {
                is Either.Right -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        existingOrder = null,
                        order = null
                    )
                }

                is Either.Left -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.value.message ?: result.value.error.defaultMessage
                    )
                }
            }
        }
    }
}