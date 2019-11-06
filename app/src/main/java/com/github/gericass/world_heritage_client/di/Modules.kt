package com.github.gericass.world_heritage_client.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.AvgleRepositoryImpl
import com.github.gericass.world_heritage_client.data.remote.BASE_URL
import com.github.gericass.world_heritage_client.home.category.CategoryViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object Modules {
    val apiModule = module {
        single {
            OkHttpClient()
                .newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(StethoInterceptor())
                //.addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                //    Timber.tag("okhttp").d(it)
                //}).apply {
                //    level = HttpLoggingInterceptor.Level.BODY
                //})
                .build()
        }

        single {
            Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }


        single {
            Retrofit.Builder()
                .client(get())
                .addConverterFactory(MoshiConverterFactory.create(get()))
                .baseUrl(BASE_URL)
                .build()
        }
    }

    val repositoryModule = module {
        single<AvgleRepository> { AvgleRepositoryImpl(get()) }
    }

    val viewModelModule = module {
        viewModel { CategoryViewModel(get()) }
    }

    //val navigationModule = module {
    //    factory<Navigtor.MainNavigator> {
    //        MainNavigator()
    //    }
    //}
}