package com.gcaguilar.randomuser.feature.user.data.repository

import com.gcaguilar.randomuser.feature.user.data.api.UserRemoteDataSource
import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed
import kotlinx.coroutines.flow.Flow

class RandomUserRepository(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) {
    suspend fun getPage(page: Int, seed: String): Result<Unit> {
        val deletedUsers = localDataSource.getDeletedUsers()
        return remoteDataSource.getUsers(page, seed)
            .mapCatching { result ->
                result.filter { user ->
                    user.uuid !in deletedUsers
                }.also {
                    localDataSource.insertAll(result)
                }
            }
    }

    fun getUsers(): Flow<List<UserModelDetailed>> {
        return localDataSource.getUsers()
    }

    suspend fun deleteUser(uuid: String) {
        localDataSource.insertInDelete(uuid)
        localDataSource.deleteUser(uuid)
    }
}
