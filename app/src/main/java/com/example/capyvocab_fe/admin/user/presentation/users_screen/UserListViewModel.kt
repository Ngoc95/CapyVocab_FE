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
            is UserListEvent.DeleteUser -> deleteUser(event.userId)
            is UserListEvent.OnDeleteSelectedUsers -> deleteSelectedUsers()
            is UserListEvent.OnSelectAllToggle -> selectAll()
            is UserListEvent.OnUserSelectToggle -> toggleUserSelection(event.userId)
            is UserListEvent.OnUserLongPress -> startMultiSelect(event.userId)
            is UserListEvent.CancelMultiSelect -> cancelMultiSelect()
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

            val avatarUrl = uploadAvatarIfNeeded(avatarUri, user.avatar) ?: return@launch

            val userToSave = user.copy(avatar = avatarUrl)

            val result = if (user.id == 0) {
                adminUserRepository.createUser(userToSave, password.orEmpty())
            } else {
                adminUserRepository.updateUser(userToSave, password)
            }

            result.fold(
                ifLeft = { failure ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = failure.message ?: "Lỗi khi lưu người dùng")
                    }
                },
                ifRight = { updatedUser ->
                    // Cập nhật user đã được sửa vào danh sách hiện tại
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

    private suspend fun uploadAvatarIfNeeded(uri: Uri?, currentAvatar: String?): String? {
        if (uri == null) return currentAvatar
        val uploadResult = adminUserRepository.uploadAvatarImage(uri)
        return uploadResult.fold(
            ifLeft = { failure ->
                _state.update {
                    it.copy(isLoading = false, errorMessage = "Upload ảnh thất bại: ${failure.message}")
                }
                null
            },
            ifRight = { url -> url }
        )
    }

    private fun deleteUser(userId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            adminUserRepository.deleteUser(userId)
                .onRight {
                    _state.update { currentState ->
                        val updatedList = currentState.users.filterNot { it.id == userId }
                        currentState.copy(
                            users = updatedList,
                            isLoading = false
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = failure.message ?: "Xoá người dùng thất bại")
                    }
                }
        }
    }

    private fun startMultiSelect(userId: Int) {
        _state.update {
            it.copy(
                isMultiSelecting = true,
                selectedUsers = setOf(userId)
            )
        }
    }

    private fun toggleUserSelection(userId: Int) {
        val currentSelected = _state.value.selectedUsers.toMutableSet()
        if (currentSelected.contains(userId)) {
            currentSelected.remove(userId)
        } else {
            currentSelected.add(userId)
        }
        _state.update { currentState ->
            currentState.copy(
                selectedUsers = currentSelected,
                isSelectAll = currentSelected.size == currentState.users.size
            )
        }
    }

    private fun selectAll() {
        val allSelected = _state.value.isSelectAll
        _state.value = if (allSelected) {
            _state.value.copy(
                selectedUsers = emptySet(),
                isSelectAll = false
            )
        } else {
            _state.value.copy(
                selectedUsers = _state.value.users.map { it.id }.toSet(),
                isSelectAll = true
            )
        }
    }

    private fun deleteSelectedUsers() {
        viewModelScope.launch {
            val selected = state.value.selectedUsers
            if (selected.isEmpty()) return@launch

            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }

            var hasError = false

            selected.forEach { userId ->
                adminUserRepository.deleteUser(userId)
                    .onLeft { hasError = true }
            }

            val remainingUsers = state.value.users.filterNot { it.id in selected }

            _state.update {
                it.copy(
                    users = remainingUsers,
                    selectedUsers = emptySet(),
                    isMultiSelecting = false,
                    isLoading = false,
                    errorMessage = if (hasError) "Một số người dùng không thể xoá" else ""
                )
            }
        }
    }

    private fun cancelMultiSelect() {
        _state.update {
            it.copy(
                isMultiSelecting = false,
                selectedUsers = emptySet(),
                isSelectAll = false,
            )
        }
    }

}