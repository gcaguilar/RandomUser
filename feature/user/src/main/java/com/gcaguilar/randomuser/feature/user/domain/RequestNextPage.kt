package com.gcaguilar.randomuser.feature.user.domain

import com.gcaguilar.randomuser.feature.user.data.repository.RandomUserRepository

class RequestNextPage(
    private val randomUserRepository: RandomUserRepository
) {
    suspend operator fun invoke(page: Int, seed: String): Result<Unit> {
        return randomUserRepository.getPage(page, seed)
    }
}