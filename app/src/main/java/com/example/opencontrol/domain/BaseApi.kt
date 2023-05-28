package com.example.opencontrol.domain

import com.example.opencontrol.model.ListKno
import com.example.opencontrol.model.ListMeasures
import retrofit2.http.GET
import retrofit2.http.Query

interface BaseApi {

    @GET("/knos")
    suspend fun getKnos(): ListKno

    @GET("/measures")
    suspend fun getMeasures(@Query("knoId") knoId: String): ListMeasures
}