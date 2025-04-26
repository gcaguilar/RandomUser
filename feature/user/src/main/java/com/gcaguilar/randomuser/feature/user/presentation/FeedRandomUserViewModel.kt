package com.gcaguilar.randomuser.feature.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcaguilar.randomuser.feature.user.domain.GetUsers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class State {
    Idle,
    Loading
}

data class UIState(
    val users: List<UserModel> = emptyList(),
    val seed: String = UUID.randomUUID().toString(),
    val page: Int = 1,
    val state: State = State.Loading,
    val searchText: String = ""
)

sealed class FeedUserIntent {
    data class TextChanged(val query: String) : FeedUserIntent()
}

class FeedRandomUserViewModel(
    private val getUsers: GetUsers
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    private val searchText: MutableStateFlow<String> = MutableStateFlow("")
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getUsers(searchText).collect { users ->
                _uiState.update {
                    it.copy(
                        users = users.toUserModel(),
                        state = State.Idle
                    )
                }
            }
        }
    }

    fun handle(intent: FeedUserIntent) {
        when (intent) {
            is FeedUserIntent.TextChanged -> onTextChange(intent.query)
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
}
