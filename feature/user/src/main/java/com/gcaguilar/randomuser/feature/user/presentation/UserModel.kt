package com.gcaguilar.randomuser.feature.user.presentation

import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed

data class UserModel(
    val uuid: String,
    val name: String,
    val surname: String,
    val email: String,
    val picture: String,
    val phone: String,
)

private fun UserModelDetailed.toUserModel(): UserModel {
    return UserModel(
        uuid = uuid,
        name = name,
        surname = surname,
        email = email,
        picture = picture,
        phone = phone
    )
}

fun List<UserModelDetailed>.toUserModel(): List<UserModel> {
    return this.map { it.toUserModel() }
}