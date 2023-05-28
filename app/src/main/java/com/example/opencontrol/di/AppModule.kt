package com.example.opencontrol.di

import com.example.opencontrol.MainViewModel
import com.example.opencontrol.domain.BaseApi
import com.example.opencontrol.domain.ChatApi
import com.example.opencontrol.domain.MainRepository
import com.example.opencontrol.domain.MainRepositoryImpl
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
    }

    single<ChatApi> {
        val retrofit = get<Retrofit>()
        retrofit.create(ChatApi::class.java)
    }

    single<BaseApi> {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl("http://178.170.195.121:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(BaseApi::class.java)
    }

    single<MainRepository> {
        MainRepositoryImpl(get(), get())
    }

    single { MainViewModel(get()) }
}
