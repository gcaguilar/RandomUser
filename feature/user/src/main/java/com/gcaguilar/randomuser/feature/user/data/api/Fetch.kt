package com.gcaguilar.randomuser.feature.user.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> HttpClient.fetch(
    crossinline block: HttpRequestBuilder.() -> Unit
): Result<T> {
    return try {
        val response: HttpResponse = request(block)
        val responseBody: T = response.body()
        Result.success(responseBody)
    } catch (e: Exception) {
        Result.failure(e)
    }
}