package com.gcaguilar.randomuser.database.di

import androidx.room.Room
import com.gcaguilar.randomuser.database.dao.DeletedDao
import com.gcaguilar.randomuser.database.dao.UserDao
import com.gcaguilar.randomuser.database.datasource.UserLocalDataSourceImpl
import com.gcaguilar.randomuser.database.db.UserDatabase
import com.gcaguilar.randomuser.userlocalstorageapi.UserLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            UserDatabase::class.java,
            "usersdb"
        ).build()
    }
    factory<UserDao> {
        get<UserDatabase>().userDao()
    }
    factory<DeletedDao> {
        get<UserDatabase>().deletedDao()
    }
    factory<UserLocalDataSource> {
        UserLocalDataSourceImpl(
            userDao = get()
        )
    }
}