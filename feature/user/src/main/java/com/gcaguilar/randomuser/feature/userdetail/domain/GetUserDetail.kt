package com.gcaguilar.randomuser.feature.userdetail.domain

import com.gcaguilar.randomuser.feature.userdetail.data.UserDetailRepository
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed

class GetUserDetail(
    private val userDetailRepository: UserDetailRepository
) {
    suspend operator fun invoke(uuid: String): Result<UserModelDetailed> {
        return userDetailRepository.getUser(uuid)
    }
}