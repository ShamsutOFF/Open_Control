package com.example.opencontrol.di

import androidx.room.Room
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.domain.AgoraApi
import com.example.opencontrol.domain.BaseApi
import com.example.opencontrol.domain.ChatApi
import com.example.opencontrol.domain.MainRepository
import com.example.opencontrol.domain.MainRepositoryImpl
import com.example.opencontrol.model.database.KnoDatabase
import com.example.opencontrol.view.chatTab.VideoScreenViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            KnoDatabase::class.java,
            "Database"
        ).build()
    }

    single { get<KnoDatabase>().knoDao() }
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
            .baseUrl("http://178.170.195.121:5000/")
//            .baseUrl("http://46.243.201.1:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(BaseApi::class.java)
    }

    single<AgoraApi> {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl("http://alexbobr.ru:8080/rtc/efe9f81f09164d32955260ccc3143b7d/publisher/userAccount/641508/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AgoraApi::class.java)
    }



    single<MainRepository> {
        MainRepositoryImpl(get(), get(), get())
    }

    single { MainViewModel(get(), get(), get()) }

    single { VideoScreenViewModel(get()) }
}
