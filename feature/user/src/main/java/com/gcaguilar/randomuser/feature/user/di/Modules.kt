package com.gcaguilar.randomuser.feature.user.di

import com.gcaguilar.randomuser.feature.user.data.api.RandomUserApiClient
import com.gcaguilar.randomuser.feature.user.data.api.UserRemoteDataSource
import com.gcaguilar.randomuser.feature.user.data.repository.RandomUserRepository
import com.gcaguilar.randomuser.feature.user.domain.DeleteUser
import com.gcaguilar.randomuser.feature.user.domain.GetUsers
import com.gcaguilar.randomuser.feature.user.domain.RequestNextPage
import com.gcaguilar.randomuser.feature.user.presentation.FeedRandomUserViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val feedDataModule = module {
    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 60000
                connectTimeoutMillis = 60000
                socketTimeoutMillis = 60000
            }
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }
        }
    }
    factory<UserRemoteDataSource> {
        RandomUserApiClient(
            client = get(),
            dispatcher = get<CoroutineDispatcher>()
        )
    }
    factory {
        RandomUserRepository(
            localDataSource = get(),
            remoteDataSource = get()
        )
    }
}

val feedDomainModule = module {
    factory {
        GetUsers(
            randomUserRepository = get()
        )
    }
    factory {
        DeleteUser(
            randomUserRepository = get()
        )
    }
    factory {
        RequestNextPage(
            randomUserRepository = get()
        )
    }
}
val feedPresentationModule = module {
    viewModelOf(::FeedRandomUserViewModel)
}