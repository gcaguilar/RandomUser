package com.gcaguilar.randomuser.feature.user.fake

import com.gcaguilar.randomuser.feature.user.mother.firstPageList
import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserLocalDataSource : UserLocalDataSource {
    var users: List<UserModelDetailed> = emptyList()
    var deletedUsers: List<String> = emptyList()

    override suspend fun insertAll(newUsers: List<UserModelDetailed>) {
        users = users.plus(newUsers)
    }

    override fun getUsers(): Flow<List<UserModelDetailed>> {
        return flowOf(firstPageList)
    }

    override suspend fun insertInDelete(uuid: String) {
        deletedUsers = deletedUsers.plus(uuid)
    }

    override suspend fun deleteUser(uuid: String) {
        users = users.filterNot { it.uuid == uuid }
    }
}
