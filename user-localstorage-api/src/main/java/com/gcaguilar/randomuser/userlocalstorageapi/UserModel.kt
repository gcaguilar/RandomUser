package com.gcaguilar.randomuser.userlocalstorageapi

data class UserModelDetailed(
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
)

data class UserModel(
    val uuid: String,
    val name: String,
    val surname: String,
    val email: String,
    val picture: String,
    val phone: String
)