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

@HiltViewModel
class PayoutViewModel @Inject constructor(
    private val payoutRepository: PayoutRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PayoutState())
    val state: StateFlow<PayoutState> = _state

    fun onEvent(event: PayoutEvent) {
        when (event) {
            is PayoutEvent.CreatePayout -> {
                createPayout()
            }

            is PayoutEvent.AmountChanged -> _state.update { it.copy(amount = event.amount.toDouble()) }
            is PayoutEvent.NameBankChanged -> _state.update { it.copy(nameBank = event.nameBank) }
            is PayoutEvent.NumberAccountChanged -> _state.update { it.copy(numberAccount = event.numberAccount) }
            is PayoutEvent.LoadMorePayouts -> loadPayouts(loadMore = true)
            is PayoutEvent.LoadPayouts -> loadPayouts()
            is PayoutEvent.UpdatePayout -> updatePayout(event.payoutId, event.status)
        }
    }

    private fun loadPayouts(loadMore: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            val nextPage = if (loadMore) _state.value.currentPage + 1 else 1
            payoutRepository.getPayouts(nextPage)
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
                            errorMessage = failure.message ?: "Failed to load payouts"
                        )
                    }
                }
        }
    }

    private fun createPayout() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            val request = PayoutRequest(
                amount = _state.value.amount,
                nameBank = _state.value.nameBank,
                numberAccount = _state.value.numberAccount
            )
            payoutRepository.createPayout(request)
                .onRight {
                    _state.update { it.copy(isLoading = false, success = true) }
                }
                .onLeft {
                    _state.update { it.copy(isLoading = false, errorMessage = it.errorMessage) }
                }
        }
    }

    private fun updatePayout(payoutId: Int, status: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
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