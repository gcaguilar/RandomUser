package com.gcaguilar.randomuser.feature.userfeed.di

import com.gcaguilar.randomuser.feature.userfeed.data.api.RandomUserApiClient
import com.gcaguilar.randomuser.feature.userfeed.data.api.UserRemoteDataSource
import com.gcaguilar.randomuser.feature.userfeed.data.repository.RandomUserRepository
import com.gcaguilar.randomuser.feature.userfeed.domain.DeleteUser
import com.gcaguilar.randomuser.feature.userfeed.domain.GetUsers
import com.gcaguilar.randomuser.feature.userfeed.domain.RequestNextPage
import com.gcaguilar.randomuser.feature.userfeed.presentation.FeedRandomUserViewModel
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