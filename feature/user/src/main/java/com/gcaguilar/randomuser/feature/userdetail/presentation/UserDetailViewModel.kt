package com.gcaguilar.randomuser.feature.userdetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcaguilar.randomuser.feature.userdetail.domain.GetUserDetail
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UIState(
    val name: String = "",
    val gender: String = "",
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val email: String = "",
    val picture: String = "",
    val screenState: State = State.Loading,
)

enum class State {
    Loading,
    Idle,
    Error
}

sealed class UserDetailIntent {
    data class GetUser(val uuid: String) : UserDetailIntent()
}

class UserDetailViewModel(
    private val getUserDetail: GetUserDetail
) : ViewModel() {
    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    fun handle(intent: UserDetailIntent) {
        when (intent) {
            is UserDetailIntent.GetUser -> requestUser(intent.uuid)
        }
    }

    private fun requestUser(uuid: String) {
        viewModelScope.launch {
            getUserDetail(uuid).fold({ user ->
                _uiState.update {
                    user.toSuccessUIState()
                }
            }, { error ->
                _uiState.update {
                    error.toErrorUIState()
                }
            })
        }
    }
}

private fun UserModelDetailed.toSuccessUIState(): UIState {
    return UIState(
        name = this.name + this.surname,
        gender = this.gender,
        street = this.street,
        city = this.city,
        state = this.state,
        email = this.email,
        picture = this.picture,
        screenState = State.Idle,
    )
}

private fun Throwable.toErrorUIState(): UIState {
    return UIState(
        name = "",
        gender = "",
        street = "",
        city = "",
        state = "",
        email = "",
        picture = "",
        screenState = State.Error,
    )
}