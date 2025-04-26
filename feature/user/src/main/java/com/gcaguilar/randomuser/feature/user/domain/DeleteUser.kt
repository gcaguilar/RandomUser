package com.gcaguilar.randomuser.feature.user.domain

import com.gcaguilar.randomuser.feature.user.data.repository.RandomUserRepository

class DeleteUser(
    private val randomUserRepository: RandomUserRepository
) {
    suspend operator fun invoke(uuid: String) {
        return randomUserRepository.deleteUser(uuid)
    }
}