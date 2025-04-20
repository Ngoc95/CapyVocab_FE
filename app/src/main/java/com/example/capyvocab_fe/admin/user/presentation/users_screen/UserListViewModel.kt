package com.example.capyvocab_fe.admin.user.presentation.users_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.user.domain.model.User
import com.example.capyvocab_fe.admin.user.domain.repository.AdminUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val adminUserRepository: AdminUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserListState())
    val state: StateFlow<UserListState> = _state

    fun onEvent(event: UserListEvent) {
        when (event) {
            is UserListEvent.LoadUsers -> {
                loadUsers()
            }

            is UserListEvent.SaveUser -> {
                saveUser(event.user, event.password, event.confirmPassword)
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            adminUserRepository.getAllUsers()
                .onRight { users ->
                    _state.update { it.copy(isLoading = false, users = users) }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = failure.message ?: "Đã xảy ra lỗi")
                    }
                }
        }
    }

    private fun saveUser(user: User, password: String?, confirmPassword: String?) {
        if (!password.isNullOrEmpty() && password != confirmPassword) {
            _state.update {
                it.copy(
                    errorMessage = "Mật khẩu và xác nhận mật khẩu không khớp"
                )
            }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            val result = if (user.id == 0) {
                adminUserRepository.createUser(user, password.orEmpty())
            } else {
                adminUserRepository.updateUser(user, password)
            }
            result.onRight {
                loadUsers()
                _state.update { it.copy(errorMessage = "") }
            }.onLeft { failure ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = failure.message ?: "Lỗi khi lưu người dùng"
                    )
                }
            }
        }
    }
}