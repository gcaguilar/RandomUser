package com.gcaguilar.randomuser.feature.user.data.mapper

import com.gcaguilar.randomuser.feature.user.data.api.RandomUserResponse
import com.gcaguilar.randomuser.feature.user.data.api.UserResponse
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed

private fun UserResponse.toUserModelDetailed(): UserModelDetailed {
    return UserModelDetailed(
        uuid = this.login.uuid,
        name = this.name.first,
        surname = this.name.last,
        email = email,
        picture = this.picture.medium,
        phone = phone,
        gender = this.gender,
        street = this.location.street.name,
        city = this.location.city,
        state = this.location.country,
        registeredDate = this.registered.date,
    )
}


fun RandomUserResponse.toUserModelDetailed(): List<UserModelDetailed> {
    return this.results.map {
        it.toUserModelDetailed()
    }
}