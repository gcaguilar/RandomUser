package com.gcaguilar.randomuser.feature.user.fake

import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed

class FakeUserLocalDataSource : UserLocalDataSource {
    private val users = mutableListOf<UserModelDetailed>()
    var lastInsertedUser: UserModelDetailed? = null

    override suspend fun insertAll(users: List<UserModelDetailed>) {
        users.plus(users)
        lastInsertedUser = users.lastOrNull()
    }
}