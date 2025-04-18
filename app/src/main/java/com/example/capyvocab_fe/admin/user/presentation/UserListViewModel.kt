package com.example.capyvocab_fe.admin.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

}