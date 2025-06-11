package com.example.capyvocab_fe.auth.presentation.register_screen

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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterViewState())
    val state = _state.asStateFlow()

    private val _navigateToOtp = MutableSharedFlow<Unit>()
    val navigateToOtp = _navigateToOtp.asSharedFlow()

    fun onUsernameChanged(newUsername: String) {
        _state.update { it.copy(username = newUsername) }
    }

    fun onEmailChanged(newEmail: String) {
        _state.update { it.copy(email = newEmail) }
    }

    fun onPasswordChanged(newPassword: String) {
        _state.update { it.copy(password = newPassword) }
    }

    fun onConfirmPasswordChanged(newConfirmPassword: String) {
        _state.update { it.copy(confirmPassword = newConfirmPassword) }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onToggleConfirmPasswordVisibility() {
        _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun register() {
        val current = _state.value

        if (current.username.isBlank() || current.email.isBlank() || current.password.isBlank()) {
            _state.update { it.copy(errorMessage = "Vui lòng nhập đầy đủ thông tin") }
            return
        }

        if (current.password != current.confirmPassword) {
            _state.update { it.copy(errorMessage = "Mật khẩu không trùng khớp") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            authRepository.register(
                username = current.username,
                email = current.email,
                password = current.password,
            ).onRight {
                _state.update { it.copy(isLoading = false, isRegistered = true) }
                _navigateToOtp.emit(Unit)  // Gửi tín hiệu điều hướng to Otp screen
            }.onLeft {
                _state.update { it.copy(isLoading = false, errorMessage = "Đăng ký thất bại") }
            }
        }
    }
}
