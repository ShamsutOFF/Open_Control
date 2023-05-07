package com.example.opencontrol.domain

import retrofit2.http.GET

interface MyApi {

    @GET("my/endpoint")
    fun callApi()
}