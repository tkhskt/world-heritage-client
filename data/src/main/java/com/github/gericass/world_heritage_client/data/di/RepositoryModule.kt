package com.github.gericass.world_heritage_client.data.di

import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.AvgleRepositoryImpl
import org.koin.dsl.module

object RepositoryModule {
    val repositoryModule = module {
        single<AvgleRepository> { AvgleRepositoryImpl(get(), get()) }
    }
}