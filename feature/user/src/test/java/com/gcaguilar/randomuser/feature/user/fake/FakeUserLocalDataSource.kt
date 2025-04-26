package com.gcaguilar.randomuser.feature.user.fake

import com.gcaguilar.randomuser.feature.user.mother.firstPageList
import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserLocalDataSource : UserLocalDataSource {
    private val users = mutableListOf<UserModelDetailed>()
    var lastInsertedUser: UserModelDetailed? = null

    override suspend fun insertAll(users: List<UserModelDetailed>) {
        users.plus(users)
        lastInsertedUser = users.lastOrNull()
    }

    override fun getUsers(): Flow<List<UserModelDetailed>> {
        return flowOf(firstPageList)
    }
}