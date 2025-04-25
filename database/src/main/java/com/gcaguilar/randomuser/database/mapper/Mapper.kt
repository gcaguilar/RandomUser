package com.gcaguilar.randomuser.database.mapper

import com.gcaguilar.randomuser.database.entity.UserEntity
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed

private fun UserModelDetailed.toUserEntity(): UserEntity {
    return UserEntity(
        uuid = this.uuid,
        name = this.name,
        surname = this.surname,
        email = email,
        picture = this.picture,
        phone = phone,
        gender = this.gender,
        street = this.street,
        city = this.city,
        state = this.state,
        registeredDate = this.registeredDate,
    )
}


fun List<UserModelDetailed>.toUserModelDetailed(): List<UserEntity> {
    return this.map { it.toUserEntity() }
}

fun UserEntity.toUserModelDetailed(): UserModelDetailed {
    return UserModelDetailed(
        uuid = this.uuid,
        name = this.name,
        surname = this.surname,
        email = email,
        picture = this.picture,
        phone = phone,
        gender = this.gender,
        street = this.street,
        city = this.city,
        state = this.state,
        registeredDate = this.registeredDate,
    )
}


fun List<UserEntity>.toUserModelDetailed(): List<UserModelDetailed> {
    return this.map { it.toUserModelDetailed() }
}