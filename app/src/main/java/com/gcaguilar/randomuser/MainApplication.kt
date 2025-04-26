package com.gcaguilar.randomuser

import android.app.Application
import com.gcaguilar.randomuser.database.di.databaseModule
import com.gcaguilar.randomuser.feature.user.di.feedDataModule
import com.gcaguilar.randomuser.feature.user.di.feedDomainModule
import com.gcaguilar.randomuser.feature.user.di.feedPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(
                databaseModule,
                feedDataModule,
                feedDomainModule,
                feedPresentationModule
            )
        }
    }
}