package com.gcaguilar.randomuser.feature.user.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.gcaguilar.randomuser.feature.user.presentation.UserModel

const val InfiniteListTag = "InfiniteList"

@Composable
fun InfiniteLazyList(
    userList: List<UserModel>,
    listState: LazyListState,
    isRequestingMoreItems: Boolean,
    onLoadMore: () -> Unit,
    onClickUser: (String) -> Unit,
    onRemoveUser: (String) -> Unit,
    modifier: Modifier = Modifier,
    buffer: Int = 2
) {
    val hitBottom: Boolean by remember {
        derivedStateOf { listState.hitBottom(buffer) }
    }

    LaunchedEffect(hitBottom) {
        if (hitBottom && !isRequestingMoreItems) {
            onLoadMore()
        }
    }

    LazyColumn(
        modifier = modifier
            .testTag(InfiniteListTag),
        state = listState,
    ) {
        items(userList.size, key = { userList[it].uuid }) {
            val user = userList[it]
            UserCard(
                name = "${user.name} ${user.surname}",
                phone = user.phone,
                picture = user.picture,
                email = user.email,
                onClickUser = { onClickUser(user.uuid) },
                onRemoveUser = { onRemoveUser(user.uuid) }
            )
            if (it != userList.size - 1) {
                HorizontalDivider()
            }
        }
    }
}

private fun LazyListState.hitBottom(buffer: Int): Boolean {
    if (layoutInfo.visibleItemsInfo.isEmpty()) return false
    val lastVisibleItem = layoutInfo.visibleItemsInfo.last()
    return lastVisibleItem.index >= layoutInfo.totalItemsCount - buffer
}

