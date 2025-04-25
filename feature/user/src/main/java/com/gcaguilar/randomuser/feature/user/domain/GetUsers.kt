package com.gcaguilar.randomuser.feature.user.domain

import com.gcaguilar.randomuser.feature.user.data.repository.RandomUserRepository
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed
import kotlinx.coroutines.flow.Flow

class GetUsers(
    private val randomUserRepository: RandomUserRepository
) {
    operator fun invoke(): Flow<List<UserModelDetailed>> {
        return randomUserRepository.getUsers()
    }
}
