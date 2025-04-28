package com.gcaguilar.randomuser

import android.app.Application
import com.gcaguilar.randomuser.database.di.databaseModule
import com.gcaguilar.randomuser.feature.userfeed.di.feedDataModule
import com.gcaguilar.randomuser.feature.userfeed.di.feedDomainModule
import com.gcaguilar.randomuser.feature.userfeed.di.feedPresentationModule
import com.gcaguilar.randomuser.feature.userfeed.di.networkModule
import com.gcaguilar.randomuser.feature.userdetail.userDetailModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(
                module { single<CoroutineDispatcher> { Dispatchers.IO } },
                networkModule,
                databaseModule,
                feedDataModule,
                feedDomainModule,
                feedPresentationModule,
                userDetailModule
            )
        }
    }
}