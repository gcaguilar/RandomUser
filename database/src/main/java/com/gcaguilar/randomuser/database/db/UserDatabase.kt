package com.gcaguilar.randomuser.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gcaguilar.randomuser.database.dao.DeletedDao
import com.gcaguilar.randomuser.database.dao.UserDao
import com.gcaguilar.randomuser.database.entity.DeleteEntity
import com.gcaguilar.randomuser.database.entity.UserEntity

@Database(entities = [UserEntity::class, DeleteEntity::class],version = 1 , exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun deletedDao(): DeletedDao
}