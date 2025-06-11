package com.example.capyvocab_fe.auth.presentation.otp_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private val _navigateToLogin = MutableSharedFlow<Unit>()
    val navigateToLogin = _navigateToLogin.asSharedFlow()

    fun onOtpChanged(newOtp: String) {
        _state.update { it.copy(otp = newOtp) }
    }

    fun reSendOtp() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            authRepository.sendVerificationEmail()
                .onRight {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Gửi mã thành công"
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Gửi mã thất bại"
                        )
                    }
                }
        }
    }

    fun verifyOtp() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            authRepository.verifyEmail(state.value.otp.toInt())
                .onRight {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Xác thực thành công"
                        )
                    }
                        _navigateToLogin.emit(Unit)  // Gửi tín hiệu
                }.onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Xác thực thất bại"
                        )
                    }
                }
        }
    }
}