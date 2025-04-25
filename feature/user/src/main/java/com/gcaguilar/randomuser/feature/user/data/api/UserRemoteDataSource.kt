package com.gcaguilar.randomuser.feature.user.data.api


import com.gcaguilar.randomuser.feature.user.data.mapper.toUserModelDetailed
import com.gcaguilar.randomuser.userlocalstorageapi.UserModelDetailed
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

private const val BASE_URL = "https://randomuser.me/api"
private const val RESULTS = 10

interface UserRemoteDataSource {
    suspend fun getUsers(page: Int, seed: String): Result<List<UserModelDetailed>>
}

class RandomUserApiClient(private val client: HttpClient) : UserRemoteDataSource {
    override suspend fun getUsers(
        page: Int,
        seed: String
    ): Result<List<UserModelDetailed>> {
        return client.fetch<RandomUserResponse> {
            url(BASE_URL)
            method = HttpMethod.Get
            parameter("page", page)
            parameter("results", RESULTS)
            parameter("seed", seed)
            parameter("format", "json")
        }.map {
            it.toUserModelDetailed()
        }
    }
}
