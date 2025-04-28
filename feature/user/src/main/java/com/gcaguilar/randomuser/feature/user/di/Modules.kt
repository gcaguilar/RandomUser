package com.gcaguilar.randomuser.feature.user.di

import com.gcaguilar.randomuser.feature.user.data.api.RandomUserApiClient
import com.gcaguilar.randomuser.feature.user.data.api.UserRemoteDataSource
import com.gcaguilar.randomuser.feature.user.data.repository.RandomUserRepository
import com.gcaguilar.randomuser.feature.user.domain.DeleteUser
import com.gcaguilar.randomuser.feature.user.domain.GetUsers
import com.gcaguilar.randomuser.feature.user.domain.RequestNextPage
import com.gcaguilar.randomuser.feature.user.presentation.FeedRandomUserViewModel
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val feedDataModule = module {
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