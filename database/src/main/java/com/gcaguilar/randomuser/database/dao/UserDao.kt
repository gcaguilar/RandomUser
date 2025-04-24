package com.gcaguilar.randomuser.database.dao

import kotlinx.coroutines.flow.Flow

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gcaguilar.randomuser.database.entity.UserEntity
import com.gcaguilar.randomuser.database.model.BasicUser

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT uuid, name, surname, email, picture, phone FROM users ORDER BY insertedAt")
    fun getAllUsers(): Flow<List<BasicUser>>

    @Query("SELECT * FROM users WHERE uuid = :uuid")
    suspend fun getUserById(uuid: String): UserEntity?

    @Query("DELETE FROM users WHERE uuid = :uuid")
    suspend fun deleteUser(uuid: String)
}