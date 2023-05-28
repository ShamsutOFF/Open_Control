package com.example.opencontrol.domain

import com.example.opencontrol.model.ListKno
import retrofit2.http.GET

interface BaseApi {

    @GET("/knos")
    suspend fun getKnos(): ListKno
}