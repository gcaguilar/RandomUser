package com.gcaguilar.randomuser.database.datasource

import com.gcaguilar.randomuser.database.dao.DeletedDao
import com.gcaguilar.randomuser.database.dao.UserDao
import com.gcaguilar.randomuser.database.entity.DeleteEntity
import com.gcaguilar.randomuser.database.mapper.toUserEntity
import com.gcaguilar.randomuser.database.mapper.toUserModelDetailed
import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserLocalDataSourceImpl(
    private val userDao: UserDao,
    private val deletedDao: DeletedDao
) : UserLocalDataSource {
    override suspend fun insertAll(users: List<UserModelDetailed>) {
        val usersEntity = users.toUserEntity()
        userDao.insertAll(usersEntity)
    }

    override fun getUsers(): Flow<List<UserModelDetailed>> {
        return userDao.getAllUsers()
            .map { it.toUserModelDetailed() }
    }

    override suspend fun insertInDelete(uuid: String) {
        return deletedDao.insert(DeleteEntity(uuid))
    }

    override suspend fun deleteUser(uuid: String) {
        return userDao.deleteUser(uuid)
        withContext(Dispatchers.IO) {
            userDao.deleteUser(uuid)
        }
    }

    override suspend fun getDeletedUsers(): List<String> {
        return withContext(Dispatchers.IO) {
            deletedDao.getAllDeletedUsers().map { it.uuid }
        }
    }
}
