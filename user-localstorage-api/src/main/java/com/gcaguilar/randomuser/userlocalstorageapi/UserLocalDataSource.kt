package com.gcaguilar.randomuser.userlocalstorageapi

import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    suspend fun insertAll(users: List<UserModelDetailed>)
    fun getUsers(): Flow<List<UserModelDetailed>>
    suspend fun insertInDelete(uuid: String)
    suspend fun deleteUser(uuid: String)
    suspend fun getDeletedUsers(): List<String>
    suspend fun getUserById(uuid: String): Result<UserModelDetailed>
}
