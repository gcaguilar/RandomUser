package com.gcaguilar.randomuser.userlocalstorageapi

import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    suspend fun insertAll(users: List<UserModelDetailed>)
    fun getUsers(): Flow<List<UserModelDetailed>>
}
