package com.gcaguilar.randomuser.feature.user.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gcaguilar.randomuser.feature.user.presentation.ui.InfiniteLazyList
import org.koin.androidx.compose.koinViewModel

@Composable
fun FeedRandomUserScreen(
    modifier: Modifier = Modifier,
    viewModel: FeedRandomUserViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        InfiniteLazyList(
            userList = state.value.users,
            listState = rememberLazyListState(),
            onClickUser = {
                // TODO
            }
        )
    }
}
