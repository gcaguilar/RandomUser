package com.gcaguilar.randomuser.feature.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcaguilar.randomuser.feature.user.domain.DeleteUser
import com.gcaguilar.randomuser.feature.user.domain.GetUsers
import com.gcaguilar.randomuser.feature.user.domain.RequestNextPage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class State {
    Loading,
    Idle,
    Error
}

data class UIState(
    val isRequestingMoreItems: Boolean = false,
    val hasErrorRequestingMoreUsers: Boolean = false,
    val users: List<UserModel> = emptyList(),
    val seed: String = UUID.randomUUID().toString(),
    val page: Int = 1,
    val state: State = State.Loading,
    val searchText: String = ""
)

sealed class FeedUserIntent {
    data class TextChanged(val query: String) : FeedUserIntent()
    data class DeleteUser(val uuid: String) : FeedUserIntent()
    data object RequestMoreUsers : FeedUserIntent()
    data object StartObserving : FeedUserIntent()
}

class FeedRandomUserViewModel(
    private val getUsers: GetUsers,
    private val deleteUser: DeleteUser,
    private val requestNextPage: RequestNextPage
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    private val searchText: MutableStateFlow<String> = MutableStateFlow("")
    val uiState = _uiState.asStateFlow()
    private val _snackbarMessage = MutableSharedFlow<Unit>(replay = 0)
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private fun startObserve() {
        viewModelScope.launch {
            getUsers(searchText).collect { users ->
                if (users.isEmpty()) {
                    onRequestNextPage()
                } else {
                    _uiState.update {
                        it.copy(
                            users = users.toUserModel(),
                            state = State.Idle
                        )
                    }
                }
            }
        }
    }


    fun handle(intent: FeedUserIntent) {
        when (intent) {
            is FeedUserIntent.TextChanged -> onTextChange(intent.query)
            is FeedUserIntent.DeleteUser -> onDeleteUser(intent.uuid)
            FeedUserIntent.RequestMoreUsers -> onRequestNextPage()
            FeedUserIntent.StartObserving -> startObserve()
        }
    }

    private fun onTextChange(query: String) {
        if (query.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    searchText = query
                )
            }
            searchText.update {
                query
            }
        } else {
            onClearInput()
        }
    }

    private fun onClearInput() {
        _uiState.update {
            it.copy(
                searchText = ""
            )
        }
        searchText.update {
            ""
        }
    }

    private fun onDeleteUser(uuid: String) {
        viewModelScope.launch {
            deleteUser(uuid)
        }
    }

    private fun onRequestNextPage() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    state = if (uiState.value.page == 1 && uiState.value.users.isEmpty()) {
                        State.Loading
                    } else {
                        State.Idle
                    },
                    hasErrorRequestingMoreUsers = false,
                    isRequestingMoreItems = true
                )
            }
            requestNextPage(uiState.value.page, uiState.value.seed)
                .fold(
                    onSuccess = { _ ->
                        _uiState.update {
                            it.copy(
                                page = it.page + 1,
                                isRequestingMoreItems = false,
                            )
                        }
                    },
                    onFailure = {
                        _uiState.update {
                            it.copy(
                                state = if (uiState.value.page == 1 && uiState.value.users.isEmpty()) {
                                    State.Error
                                } else {
                                    State.Idle
                                },
                                isRequestingMoreItems = false,
                                hasErrorRequestingMoreUsers = true
                            )
                        }
                        _snackbarMessage.emit(Unit)
                    }
                )
        }
    }
}
