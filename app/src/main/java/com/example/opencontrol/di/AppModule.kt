package com.example.opencontrol.di

import com.example.opencontrol.MainViewModel
import com.example.opencontrol.domain.MainRepository
import com.example.opencontrol.domain.MyApi
import com.example.opencontrol.domain.OnlineMainRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl("http://178.170.194.57:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MyApi::class.java)
    }
    single<MainRepository> {
        OnlineMainRepositoryImpl(get())
    }
    single { MainViewModel(get()) }
//    viewModel { MainViewModel(get()) }
}