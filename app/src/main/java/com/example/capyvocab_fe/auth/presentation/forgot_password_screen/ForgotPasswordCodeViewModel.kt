package com.example.capyvocab_fe.auth.presentation.forgot_password_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordCodeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordCodeState())
    val state = _state.asStateFlow()

    private var countdownJob: Job? = null

    init {
        startCountdown()
    }

    fun onCodeChanged(value: String) {
        _state.update { it.copy(code = value) }
    }

    fun onNewPasswordChanged(value: String) {
        _state.update { it.copy(newPassword = value) }
    }

    fun onConfirmPasswordChanged(value: String) {
        _state.update { it.copy(confirmPassword = value) }
    }

    fun resetPassword(email: String) {
        val current = _state.value
        if (current.newPassword != current.confirmPassword) {
            _state.update { it.copy(errorMessage = "Mật khẩu xác nhận không khớp") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            authRepository.changePassword(email, current.code.trim(), current.newPassword.trim())
                .onRight {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = failure.message ?: "Có lỗi xảy ra.")
                    }
                }
        }
    }

    fun resendCode(email: String) {
        if (_state.value.canResend) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, errorMessage = "") }
                authRepository.sendChangePasswordEmail(email)
                    .onRight {
                        // Khi thành công, reset đếm giờ
                        _state.update { it.copy(isLoading = false) }
                        startCountdown()
                    }
                    .onLeft { failure ->
                        _state.update {
                            it.copy(isLoading = false, errorMessage = failure.message ?: "Gửi lại mã thất bại.")
                        }
                    }
            }
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        _state.update { it.copy(remainingTime = 300, canResend = false) }
        countdownJob = viewModelScope.launch {
            while (_state.value.remainingTime > 0) {
                delay(1000)
                _state.update {
                    it.copy(remainingTime = it.remainingTime - 1)
                }
            }
            _state.update { it.copy(canResend = true) }
        }
    }
}