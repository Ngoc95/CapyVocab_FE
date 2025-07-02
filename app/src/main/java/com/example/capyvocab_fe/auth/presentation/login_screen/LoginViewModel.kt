package com.example.capyvocab_fe.auth.presentation.login_screen

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginViewState())
    val state = _state.asStateFlow()

    private val _navigateToAdmin = MutableSharedFlow<Unit>()
    val navigateToAdmin = _navigateToAdmin.asSharedFlow()

    private val _navigateToUser = MutableSharedFlow<Unit>()
    val navigateToUser = _navigateToUser.asSharedFlow()

    private val _navigateToOtp = MutableSharedFlow<Unit>()
    val navigateToOtp = _navigateToOtp.asSharedFlow()

    fun onUsernameChanged(newUsername: String) {
        _state.update { it.copy(username = newUsername) }
    }

    fun onPasswordChanged(newPassword: String) {
        _state.update { it.copy(password = newPassword) }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            authRepository.login(_state.value.username, _state.value.password)
                .onRight { user ->
                    // Kiểm tra status trước khi chuyển hướng
                    if (user.status == "NOT_VERIFIED") {
                        authRepository.sendVerificationEmail()
                        _navigateToOtp.emit(Unit)  // Chuyển đến OTP screen nếu chưa verify
                    } else {
                        // Kiểm tra roleId và chuyển hướng
                        if (user.roleId == 1) {
                            _navigateToAdmin.emit(Unit)  // Gửi tín hiệu điều hướng đến Admin
                        } else {
                            _navigateToUser.emit(Unit)   // Gửi tín hiệu điều hướng đến User
                        }
                    }

                    _state.update { it.copy(isLoading = false, isLoggedIn = true) }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Đã xảy ra lỗi"
                        )
                    }
                }
        }
    }

    fun clearForm() {
        _state.update { current ->
            current.copy(
                username = "",
                password = "",
                isPasswordVisible = false
            )
        }
    }

    fun googleLogin(token: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            authRepository.googleLogin(token)
                .onRight { user ->
                    // Kiểm tra roleId và chuyển hướng
                    if (user.roleId == 1) {
                        _navigateToAdmin.emit(Unit)  // Gửi tín hiệu điều hướng đến Admin
                    } else {
                        _navigateToUser.emit(Unit)   // Gửi tín hiệu điều hướng đến User
                    }


                    _state.update { it.copy(isLoading = false, isLoggedIn = true) }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message
                                ?: "Đã xảy ra lỗi khi đăng nhập bằng Google"
                        )
                    }
                }
        }
    }
}