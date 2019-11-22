package com.github.gericass.world_heritage_client

import android.app.Application
import com.facebook.stetho.Stetho
import com.github.gericass.world_heritage_client.di.Modules.apiModule
import com.github.gericass.world_heritage_client.di.Modules.databaseModule
import com.github.gericass.world_heritage_client.di.Modules.navigatorModule
import com.github.gericass.world_heritage_client.di.Modules.repositoryModule
import com.github.gericass.world_heritage_client.di.Modules.viewModelModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Android context
            androidContext(this@App)
            modules(
                listOf(
                    apiModule,
                    repositoryModule,
                    viewModelModule,
                    databaseModule,
                    navigatorModule
                )
            )
        }
        AndroidThreeTen.init(this)
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }
    }
}