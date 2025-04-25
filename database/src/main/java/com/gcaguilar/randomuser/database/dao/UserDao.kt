package com.gcaguilar.randomuser.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gcaguilar.randomuser.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users ORDER BY insertedAt")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE uuid = :uuid")
    suspend fun getUserById(uuid: String): UserEntity?

    @Query("DELETE FROM users WHERE uuid = :uuid")
    suspend fun deleteUser(uuid: String)
}