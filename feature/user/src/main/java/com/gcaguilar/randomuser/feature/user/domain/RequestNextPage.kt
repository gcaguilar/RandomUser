package com.gcaguilar.randomuser.feature.user.domain

import android.util.Log
import com.gcaguilar.randomuser.feature.user.data.repository.RandomUserRepository
import kotlinx.io.IOException

class RequestNextPage(
    private val randomUserRepository: RandomUserRepository
) {
    suspend fun invoke(page: Int, seed: String) {
        randomUserRepository.getPage(page, seed)
            .recover {
                Log.e(
                    RequestNextPage::class.simpleName,
                    it.message ?: "Failed to request next page"
                )
                IOException("Failed to request next page")
            }
    }
}