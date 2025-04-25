package com.gcaguilar.randomuser.userlocalstorageapi

interface UserLocalDataSource {
    suspend fun insertAll(users: List<UserModelDetailed>)
}