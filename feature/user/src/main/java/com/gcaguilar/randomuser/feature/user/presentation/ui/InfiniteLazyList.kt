package com.gcaguilar.randomuser.feature.user.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.gcaguilar.randomuser.feature.user.presentation.UserModel

const val InfiniteListTag = "InfiniteList"

@Composable
fun InfiniteLazyList(
    userList: List<UserModel>,
    listState: LazyListState,
    onClickUser: (String) -> Unit,
    onRemoveUser: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
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

