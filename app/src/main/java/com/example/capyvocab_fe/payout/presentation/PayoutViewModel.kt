package com.example.capyvocab_fe.payout.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.payout.data.model.PayoutRequest
import com.example.capyvocab_fe.payout.domain.repository.PayoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.ui.text.input.TextFieldValue
import com.example.capyvocab_fe.payout.domain.model.PayoutStatus

@HiltViewModel
class PayoutViewModel @Inject constructor(
    private val payoutRepository: PayoutRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PayoutState())
    val state: StateFlow<PayoutState> = _state

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            _state.update { it.copy(errorMessage = "", successMessage = "") }
            payoutRepository.getUserInfo().fold(
                { failure ->
                    _state.update { it.copy(errorMessage = failure.message ?: "Đã xảy ra lỗi") }
                },
                { user ->
                    _state.update {
                        it.copy(
                            currentUser = user
                        )
                    }
                }
            )
        }
    }

    fun onEvent(event: PayoutEvent) {
        when (event) {
            is PayoutEvent.CreatePayout -> createPayout()
            is PayoutEvent.AmountChanged -> _state.update { it.copy(amount = event.amount) }
            is PayoutEvent.NameBankChanged -> _state.update { it.copy(nameBank = event.nameBank) }
            is PayoutEvent.NumberAccountChanged -> _state.update { it.copy(numberAccount = event.numberAccount) }
            is PayoutEvent.LoadMorePayouts -> loadPayouts(loadMore = true)
            is PayoutEvent.LoadPayouts -> loadPayouts()
            is PayoutEvent.UpdatePayout -> updatePayout(event.payoutId, event.status)
            is PayoutEvent.PayoutStatusChanged -> {
                _state.update { it.copy(selectedStatus = event.status, payouts = emptyList(), currentPage = 1, isEndReached = false) }
                loadPayouts(status = event.status)
            }
        }
    }

    private fun loadPayouts(loadMore: Boolean = false, status: PayoutStatus? = null) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "", successMessage = "") }
            val nextPage = if (loadMore) _state.value.currentPage + 1 else 1
            val currentStatus = status ?: _state.value.selectedStatus
            payoutRepository.getPayouts(nextPage, status = currentStatus.name)
                .onRight { newPayouts ->
                    _state.update {
                        val allPayouts = if (loadMore) it.payouts + newPayouts else newPayouts
                        it.copy(
                            isLoading = false,
                            payouts = allPayouts,
                            currentPage = nextPage,
                            isEndReached = newPayouts.isEmpty()
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Failed to load payouts",
                            successMessage = ""
                        )
                    }
                }
        }
    }

    private fun createPayout() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "", successMessage = "") }
            val amountDouble = _state.value.amount.text.replace(",", "").toDoubleOrNull()
            if (amountDouble == null || amountDouble <= 0.0) {
                _state.update { it.copy(isLoading = false, errorMessage = "Số tiền không hợp lệ") }
                return@launch
            }
            val request = PayoutRequest(
                amount = amountDouble,
                nameBank = _state.value.nameBank,
                numberAccount = _state.value.numberAccount
            )
            payoutRepository.createPayout(request)
                .onRight {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Yêu cầu rút tiền thành công",
                            amount = TextFieldValue(""),
                            numberAccount = "",
                            nameBank = "",
                        )
                    }
                    getCurrentUser()
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Xảy ra lỗi khi yêu cầu rút tiền"
                        )
                    }
                }
        }
    }

    private fun updatePayout(payoutId: Int, status: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "", successMessage = "") }
            payoutRepository.updatePayout(payoutId, status)
                .onRight { updatedPayout ->
                    _state.update { currentState ->
                        val updatedPayouts = currentState.payouts.map {
                            if (it.id == updatedPayout.id) updatedPayout else it
                        }
                        currentState.copy(
                            isLoading = false,
                            payouts = updatedPayouts
                        )
                    }
                    loadPayouts()
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Failed to update payout"
                        )
                    }
                }
        }
    }
}