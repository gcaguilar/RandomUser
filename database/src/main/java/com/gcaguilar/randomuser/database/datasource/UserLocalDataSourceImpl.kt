package com.gcaguilar.randomuser.database.datasource

import android.content.res.Resources.NotFoundException
import com.gcaguilar.randomuser.database.dao.DeletedDao
import com.gcaguilar.randomuser.database.dao.UserDao
import com.gcaguilar.randomuser.database.entity.DeleteEntity
import com.gcaguilar.randomuser.database.mapper.toUserEntity
import com.gcaguilar.randomuser.database.mapper.toUserModelDetailed
import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserLocalDataSourceImpl(
    private val userDao: UserDao,
    private val deletedDao: DeletedDao,
    private val dispatcher: CoroutineDispatcher
) : UserLocalDataSource {
    override suspend fun insertAll(users: List<UserModelDetailed>) {
        withContext(dispatcher) {
            val usersEntity = users.toUserEntity()
            userDao.insertAll(usersEntity)
        }
    }

    override fun getUsers(): Flow<List<UserModelDetailed>> {
        return userDao.getAllUsers()
            .flowOn(dispatcher)
            .map { it.toUserModelDetailed() }
    }

    override suspend fun insertInDelete(uuid: String) {
        withContext(dispatcher) {
            deletedDao.insert(DeleteEntity(uuid))
        }
    }

    override suspend fun deleteUser(uuid: String) {
        withContext(dispatcher) {
            userDao.deleteUser(uuid)
        }
    }

    override suspend fun getDeletedUsers(): List<String> {
        return withContext(dispatcher) {
            deletedDao.getAllDeletedUsers().map { it.uuid }
        }
    }

    override suspend fun getUserById(uuid: String): Result<UserModelDetailed> {
        return withContext(dispatcher) {
            try {
                userDao.getUserById(uuid)?.let {
                    Result.success(it.toUserModelDetailed())
                } ?: run {
                    Result.failure(NotFoundException("$uuid not found"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
