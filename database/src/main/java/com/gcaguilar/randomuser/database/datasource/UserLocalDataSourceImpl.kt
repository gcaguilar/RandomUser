package com.gcaguilar.randomuser.database.datasource

import com.gcaguilar.randomuser.database.dao.DeletedDao
import com.gcaguilar.randomuser.database.dao.UserDao
import com.gcaguilar.randomuser.database.entity.DeleteEntity
import com.gcaguilar.randomuser.database.mapper.toUserEntity
import com.gcaguilar.randomuser.database.mapper.toUserModelDetailed
import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserLocalDataSourceImpl(
    private val userDao: UserDao,
    private val deletedDao: DeletedDao
) : UserLocalDataSource {
    override suspend fun insertAll(users: List<UserModelDetailed>) {
        withContext(Dispatchers.IO) {
            val usersEntity = users.toUserEntity()
            userDao.insertAll(usersEntity)
        }
    }

    override fun getUsers(): Flow<List<UserModelDetailed>> {
        return userDao.getAllUsers()
            .flowOn(Dispatchers.IO)
            .map { it.toUserModelDetailed() }
    }

    override suspend fun insertInDelete(uuid: String) {
        withContext(Dispatchers.IO) {
            deletedDao.insert(DeleteEntity(uuid))
        }
    }

    override suspend fun deleteUser(uuid: String) {
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
