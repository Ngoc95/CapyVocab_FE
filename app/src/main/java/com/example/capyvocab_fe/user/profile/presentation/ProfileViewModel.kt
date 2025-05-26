package com.example.capyvocab_fe.user.profile.presentation;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.auth.domain.repository.AuthRepository
import com.example.capyvocab_fe.user.learn.presentation.LearnEvent
import com.example.capyvocab_fe.user.profile.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository:AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadCurrentUser -> loadCurrentUser()

            is ProfileEvent.ClearError -> _state.update { it.copy(errorMessage = "") }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            authRepository.getUserInfo()
                .onRight { user ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            currentUser = user
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Failed to load user"
                        )
                    }
                }
        }
    }
}

