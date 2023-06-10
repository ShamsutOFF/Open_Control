package com.example.opencontrol.domain

import com.example.opencontrol.model.networkDTOs.AgoraToken
import com.example.opencontrol.model.networkDTOs.ListKno
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AgoraApi {
    @GET("/{userId}")
    suspend fun getAgoraToken(@Path("userId") userId: String): AgoraToken
}
