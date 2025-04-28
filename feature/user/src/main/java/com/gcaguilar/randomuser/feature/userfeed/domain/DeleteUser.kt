package com.gcaguilar.randomuser.feature.userfeed.domain

import com.gcaguilar.randomuser.feature.userfeed.data.repository.RandomUserRepository

class DeleteUser(
    private val randomUserRepository: RandomUserRepository
) {
    suspend operator fun invoke(uuid: String) {
        return randomUserRepository.deleteUser(uuid)
    }
}