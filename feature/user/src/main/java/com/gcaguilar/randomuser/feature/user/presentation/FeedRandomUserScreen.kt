package com.gcaguilar.randomuser.feature.user.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gcaguilar.randomuser.feature.navigation.UserDetail
import com.gcaguilar.randomuser.feature.user.presentation.ui.InfiniteLazyList
import com.gcaguilar.randomuser.feature.user.presentation.ui.SearchBox
import com.gcaguilar.randomuser.ui.theme.ui.Error
import com.gcaguilar.randomuser.ui.theme.ui.Loading
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

private val SnackBarEvent = "SnackBarEvent"

@Composable
fun FeedRandomUserScreen(
    onReceiveError: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: FeedRandomUserViewModel = koinViewModel()
) {
    val listState = rememberLazyListState()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(SnackBarEvent) {
        viewModel.snackbarMessage.collectLatest {
            onReceiveError()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.handle(FeedUserIntent.StartObserving)
    }

    when (state.value.state) {
        State.Loading -> Loading(modifier.fillMaxSize())
        State.Idle -> UserList(
            modifier = modifier.fillMaxSize(),
            searchText = state.value.searchText,
            userList = state.value.users,
            listState = listState,
            isRequestingMoreItems = state.value.isRequestingMoreItems,
            onTextChanged = { viewModel.handle(FeedUserIntent.TextChanged(it)) },
            onRemoveUser = { viewModel.handle(FeedUserIntent.DeleteUser(it)) },
            onLoadMore = { viewModel.handle(FeedUserIntent.RequestMoreUsers) },
            onClickUser = { navController.navigate(UserDetail(it)) },
        )

        State.Error -> Error(
            onRetry = {
                viewModel.handle(FeedUserIntent.RequestMoreUsers)
            }
        )
    }
}

@Composable
fun UserList(
    modifier: Modifier,
    searchText: String,
    isRequestingMoreItems: Boolean,
    listState: LazyListState,
    userList: List<UserModel>,
    onTextChanged: (String) -> Unit,
    onClickUser: (String) -> Unit,
    onRemoveUser: (String) -> Unit,
    onLoadMore: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        SearchBox(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            value = searchText,
            onValueChange = onTextChanged,
        )
        InfiniteLazyList(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            userList = userList,
            listState = listState,
            onClickUser = onClickUser,
            onRemoveUser = onRemoveUser,
            onLoadMore = onLoadMore,
            isRequestingMoreItems = isRequestingMoreItems
        )

        AnimatedVisibility(isRequestingMoreItems) {
            Spacer(Modifier.height(16.dp))
            Loading(modifier = Modifier)
        }
    }
}
