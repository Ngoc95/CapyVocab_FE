package com.example.capyvocab_fe.admin.user.presentation.users_screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
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
                saveUser(event.user, event.password, event.confirmPassword, event.avatarUri)
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

    private fun saveUser(user: User, password: String?, confirmPassword: String?, avatarUri: Uri?) {
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

            // Upload ảnh nếu có uri
            val avatarUrl = avatarUri?.let {
                adminUserRepository.uploadAvatarImage(it).getOrElse { error ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = "Upload ảnh thất bại: ${error.message}")
                    }
                    return@launch
                }
            } ?: user.avatar

            val userToSave = user.copy(avatar = avatarUrl)

            val result = if (user.id == 0) {
                adminUserRepository.createUser(userToSave, password.orEmpty())
            } else {
                adminUserRepository.updateUser(userToSave, password)
            }

            result.onRight {
                loadUsers()
                _state.update { it.copy(errorMessage = "") }
            }.onLeft { failure ->
                _state.update {
                    it.copy(isLoading = false, errorMessage = failure.message ?: "Lỗi khi lưu người dùng")
                }
            }
        }
    }
}