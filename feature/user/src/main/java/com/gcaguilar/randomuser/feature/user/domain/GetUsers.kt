package com.gcaguilar.randomuser.feature.user.domain

import com.gcaguilar.randomuser.feature.user.data.repository.RandomUserRepository
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

class GetUsers(
    private val randomUserRepository: RandomUserRepository
) {
    operator fun invoke(searchText: StateFlow<String>): Flow<List<UserModelDetailed>> {
        return combine(
            searchText,
            randomUserRepository.getUsers()
        ) { text, users ->
            users.let { userList ->
                if (text.trimIndent().isNotEmpty()) {
                    userList.filter { user ->
                        user.name.contains(text, ignoreCase = true) ||
                        user.surname.contains(text, ignoreCase = true) ||
                        user.email.contains(text, ignoreCase = true)
                    }
                } else {
                    userList
                }
            }
        }
    }
}
