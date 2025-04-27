package com.gcaguilar.randomuser.feature.userdetail.data

import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed

class UserDetailRepository(
    private val localDataSource: UserLocalDataSource
) {
    suspend fun getUser(uuid: String): Result<UserModelDetailed> {
        return localDataSource.getUserById(uuid)
    }
}
