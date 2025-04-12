package com.example.capyvocab_fe.auth.presentation.login_screen

import android.util.Log
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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(LoginViewState())
    val state = _state.asStateFlow()


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
                    .onRight {
                        _state.update { it.copy(isLoading = false, isLoggedIn = true) }
                    }
                    .onLeft {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Invalid credentials"
                            )
                        }
                    }
        }
    }
}