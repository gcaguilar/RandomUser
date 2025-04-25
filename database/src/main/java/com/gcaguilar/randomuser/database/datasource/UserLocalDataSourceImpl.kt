package com.gcaguilar.randomuser.database.datasource

import com.gcaguilar.randomuser.database.dao.UserDao
import com.gcaguilar.randomuser.database.mapper.toUserEntity
import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed

class UserLocalDataSourceImpl(
    private val userDao: UserDao
): UserLocalDataSource {
    override suspend fun insertAll(users: List<UserModelDetailed>) {
        val usersEntity = users.toUserEntity()
        userDao.insertAll(usersEntity)
    }
}