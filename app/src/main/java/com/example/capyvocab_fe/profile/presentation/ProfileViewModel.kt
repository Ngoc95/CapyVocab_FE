package com.example.capyvocab_fe.profile.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.user.domain.repository.AdminUserRepository
import com.example.capyvocab_fe.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val adminUserRepository: AdminUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadProfile -> loadProfile()
            is ProfileEvent.Logout -> logout()
            is ProfileEvent.UpdateProfile -> updateProfile(event.avatar, event.email, event.username)
            is ProfileEvent.DeleteAccount -> {}
            is ProfileEvent.ChangePassword -> changePassword(event.oldPassword, event.newPassword)
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            profileRepository.getProfile()
                .onRight { user ->
                    _state.update { it.copy(user = user, isLoading = false) }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(errorMessage = failure.message ?: "Đã xảy ra lỗi", isLoading = false)
                    }
                }
        }
    }

    private fun updateProfile(avatar: Any?, email: String, username: String) {
        viewModelScope.launch {
            val userId = _state.value.user?.id ?: return@launch
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val avatarUrl = when (avatar) {
                is Uri -> {
                    adminUserRepository.uploadAvatarImage(avatar).getOrNull()
                }
                is String -> avatar
                else -> _state.value.user?.avatar
            }

            profileRepository.updateProfile(userId, avatarUrl, email, username)
                .onRight { updated ->
                    _state.update { it.copy(user = updated, isLoading = false) }
                }
                .onLeft { failure ->
                    _state.update { it.copy(errorMessage = failure.message, isLoading = false) }
                }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            val userId = _state.value.user?.id ?: return@launch
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            profileRepository.updatePassword(userId, oldPassword, newPassword)
                .onRight {
                    _state.update { it.copy(isLoading = false) }
                }
                .onLeft { failure ->
                    _state.update { it.copy(errorMessage = failure.message, isLoading = false) }
                }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            profileRepository.logout()
                .onRight {
                    _state.update {
                        ProfileState() // Reset toàn bộ state sau khi logout
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(errorMessage = failure.message, isLoading = false)
                    }
                }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
