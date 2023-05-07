package com.example.opencontrol.di

import com.example.opencontrol.MainViewModel
import com.example.opencontrol.domain.MainRepository
import com.example.opencontrol.domain.MainRepositoryImpl
import com.example.opencontrol.domain.MyApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://google.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
    }
    single<MainRepository> {
        MainRepositoryImpl(get())
    }
    single { MainViewModel(get()) }
//    viewModel { MainViewModel(get()) }
}