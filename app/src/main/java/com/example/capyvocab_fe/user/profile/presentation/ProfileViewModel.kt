//package com.example.capyvocab_fe.user.profile.presentation;
//
//import androidx.core.net.toUri
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.capyvocab_fe.core.data.TokenManager
//import com.example.capyvocab_fe.user.profile.domain.model.ProfileUser
//import com.example.capyvocab_fe.user.profile.domain.repository.UserProfileRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//@HiltViewModel
//class ProfileViewModel @Inject constructor(
//    private val userProfileRepository: UserProfileRepository,
//    private val tokenManager: TokenManager,
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(ProfileState())
//    val state: StateFlow<ProfileState> = _state
//
//
//    fun onEvent(event: ProfileEvent) {
//        when (event) {
//            is ProfileEvent.GetCurrentUser -> loadCurrentUser()
//            is ProfileEvent.ClearError -> _state.update { it.copy(errorMessage = "") }
//            is ProfileEvent.Logout -> logout()
//            is ProfileEvent.UpdateUser -> updateUser(event.user)
//            is ProfileEvent.DeleteUser -> deleteUser()
//            is ProfileEvent.PasswordChange -> { }
//            else -> {}
//        }
//    }
//
//
//
//    private fun deleteUser() {
//        viewModelScope.launch {
//            val user = _state.value.currentUser
//            if (user != null) {
//                userProfileRepository.logout(user)
//                    .onRight {
//                        _state.update {
//                            it.copy(
//                                userLogouted = true,
//                            )
//                        }
//                    }.onLeft { failure ->
//                        _state.update {
//                            it.copy(
//                                isLoading = false,
//                                errorMessage = failure.message ?: "Đã xảy ra lỗi"
//                            )
//                    }
//                if(_state.value.userDeleted)
//                {
//                    userProfileRepository.deleteUser(user)
//                        .onRight {
//                            _state.update {
//                                it.copy(
//                                    userDeleted = true,
//                                )
//                            }
//                        }.onLeft { failure ->
//                            _state.update {
//                                it.copy(
//                                    isLoading = false,
//                                    errorMessage = failure.message ?: "Đã xảy ra lỗi"
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun updateUser(user: ProfileUser)  {
//        viewModelScope.launch {
//            _state.update {
//                it.copy(
//                    isLoading = true,
//                    errorMessage = "",
//                )
//            }
//            if(user.avatar != null && user.avatar != _state.value.currentUser?.avatar)
//            {
//                var avatar: String = ""
//                var updateUser: ProfileUser;
//                val uri = user.avatar.toUri();
//                val uploadResult = userProfileRepository.uploadImage(uri);
//                avatar = uploadResult.fold(
//                    ifLeft = { failure ->
//                        _state.update {
//                            it.copy(
//                                isLoading = false,
//                                errorMessage = failure.message ?: "Có lỗi xảy ra"
//                            )
//                        }.toString()
//                    },
//                    ifRight = { urls -> urls }
//                )
//
//                if(avatar != "")
//                {
//                    updateUser = ProfileUser(
//                        id = user.id,
//                        email = user.email,
//                        avatar = avatar,
//                        username = user.username,
//                        status = user.status,
//                        streak = user.streak,
//                        lastStudyDate = user.lastStudyDate,
//                        totalStudyDay = user.totalStudyDay,
//                    )
//                }
//                else
//                {
//                    updateUser = user;
//                }
//                userProfileRepository.updateUser(updateUser)
//                .onRight{ user ->
//                    _state.update {
//
//                        it.copy(
//                            currentUser = user,
//                            isLoading = false
//                        )
//                    }
//
//
//                }
//                .onLeft { failure ->
//                    _state.update {
//                        it.copy(
//                            isLoading = false,
//                            errorMessage = failure.message ?: "Đã xảy ra lỗi"
//                        )
//                    }
//                }
//            }
//            else
//            {
//                userProfileRepository.updateUser(user)
//                .onRight{ user ->
//                    _state.update {
//                        it.copy(
//                            currentUser = user,
//                            isLoading = false
//                        )
//                    }
//
//
//                }
//                .onLeft { failure ->
//                    _state.update {
//                        it.copy(
//                            isLoading = false,
//                            errorMessage = failure.message ?: "Đã xảy ra lỗi"
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    private fun logout() {
//        viewModelScope.launch {
//            val user = _state.value.currentUser
//            if (user != null) {
//                userProfileRepository.logout(user)
//                    .onRight {
//                        _state.update {
//                            it.copy(
//                                userLogouted = true,
//                            )
//                        }
//                    }.onLeft { failure ->
//                        _state.update {
//                            it.copy(
//                                isLoading = false,
//                                errorMessage = failure.message ?: "Đã xảy ra lỗi"
//                            )
//
//                        }
//                    }
//            }
//        }
//    }
//    private fun loadCurrentUser() {
//        viewModelScope.launch {
//            _state.update { it.copy(isLoading = true, errorMessage = "") }
//            val id = tokenManager.userId.first()
//            if(id != null) {
//                userProfileRepository.getUserInfor(id)
//                    .onRight { user ->
//                        _state.update {
//                            it.copy(
//                                isLoading = false,
//                                currentUser = user
//                            )
//                        }
//                    }
//                    .onLeft { failure ->
//                        _state.update {
//                            it.copy(
//                                isLoading = false,
//                                errorMessage = failure.message ?: "Không tìm thấy User"
//                            )
//                        }
//                    }
//            } else {
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        errorMessage = "Không tìm thấy User"
//                    )
//                }
//            }
//        }
//    }
//}
//
