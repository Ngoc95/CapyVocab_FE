package com.example.capyvocab_fe.auth.presentation.forgot_password_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ForgotPasswordEmailState())
    val state = _state.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _state.update { it.copy(email = newEmail) }
    }

    fun sendEmail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            authRepository.sendChangePasswordEmail(_state.value.email.trim())
                .onRight {
                    _state.update { it.copy(isLoading = false, isEmailSent = true) }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = failure.message ?: "Có lỗi xảy ra")
                    }
                }
        }
    }
}
