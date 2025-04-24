package com.gcaguilar.randomuser.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val uuid: String,
    val name: String,
    val surname: String,
    val email: String,
    val picture: String,
    val phone: String,
    val gender: String,
    val street: String,
    val city: String,
    val state: String,
    val registeredDate: String,
    val insertedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "deleted")
data class DeleteEntity(
    @PrimaryKey
    val uuid: String,
)
