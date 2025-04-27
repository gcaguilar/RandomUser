package com.gcaguilar.randomuser.feature.userdetail

import com.gcaguilar.randomuser.feature.userdetail.data.UserDetailRepository
import com.gcaguilar.randomuser.feature.userdetail.domain.GetUserDetail
import com.gcaguilar.randomuser.feature.userdetail.presentation.UserDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userDetailModule = module {
    factory {
        UserDetailRepository(get())
    }
    factory {
        GetUserDetail(get())
    }
    viewModelOf(::UserDetailViewModel)
}