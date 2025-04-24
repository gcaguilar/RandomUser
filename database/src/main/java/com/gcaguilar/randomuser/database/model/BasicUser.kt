package com.gcaguilar.randomuser.database.model

import androidx.room.ColumnInfo

data class BasicUser(
    @ColumnInfo(name = "uuid") val uuid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "surname") val surname: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "picture") val picture: String,
    @ColumnInfo(name = "phone") val phone: String,
)