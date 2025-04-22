package com.example.capyvocab_fe.admin.user.presentation.users_screen

import android.net.Uri
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
            is UserListEvent.LoadUsers -> loadUsers()
            is UserListEvent.LoadMoreUsers -> loadUsers(loadMore = true)
            is UserListEvent.SaveUser -> saveUser(event.user, event.password, event.confirmPassword, event.avatarUri)
        }
    }

    private fun loadUsers(loadMore: Boolean = false) {
        viewModelScope.launch {
            val nextPage = if (loadMore) state.value.currentPage + 1 else 1

            _state.update { it.copy(isLoading = true, errorMessage = "") }

            adminUserRepository.getAllUsers(nextPage)
                .onRight { newUsers ->
                    _state.update {
                        val allUsers = if (loadMore) it.users + newUsers else newUsers
                        it.copy(
                            isLoading = false,
                            users = allUsers,
                            currentPage = nextPage,
                            isEndReached = newUsers.isEmpty()
                        )
                    }
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
                it.copy(errorMessage = "Mật khẩu và xác nhận mật khẩu không khớp")
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            // 1. Upload avatar nếu có ảnh mới
            val avatarUrl = if (avatarUri != null) {
                val uploadResult = adminUserRepository.uploadAvatarImage(avatarUri)
                uploadResult.fold(
                    ifLeft = { failure ->
                        _state.update {
                            it.copy(isLoading = false, errorMessage = "Upload ảnh thất bại: ${failure.message}")
                        }
                        return@launch
                    },
                    ifRight = { url -> url }
                )
            } else {
                user.avatar
            }

            // 2. Gọi create/update
            val userToSave = user.copy(avatar = avatarUrl)
            val result = if (user.id == 0) {
                adminUserRepository.createUser(userToSave, password.orEmpty())
            } else {
                adminUserRepository.updateUser(userToSave, password)
            }

            // 3. Cập nhật lại danh sách
            result.fold(
                ifLeft = { failure ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = failure.message ?: "Lỗi khi lưu người dùng")
                    }
                },
                ifRight = { updatedUser ->
                    // Update user trong danh sách hiện tại
                    _state.update { currentState ->
                        val updatedUsers = currentState.users.map {
                            if (it.id == updatedUser.id) updatedUser else it
                        }
                        currentState.copy(
                            isLoading = false,
                            errorMessage = "",
                            users = updatedUsers
                        )
                    }
                }

            )
        }
    }
}