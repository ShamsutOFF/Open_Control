package com.example.opencontrol.domain

import com.example.opencontrol.model.networkDTOs.IdNetwork
import com.example.opencontrol.model.networkDTOs.ListFreeWindows
import com.example.opencontrol.model.networkDTOs.ListKno
import com.example.opencontrol.model.networkDTOs.ListMeasures
import com.example.opencontrol.model.networkDTOs.NoteInfoForConsultationNetwork
import com.example.opencontrol.model.networkDTOs.UserRegisterInfoNetwork
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface BaseApi {

    @GET("/info/knos")
    suspend fun getKnos(): ListKno

    @GET("/info/measures")
    suspend fun getMeasures(@Query("knoId") knoId: String): ListMeasures

    //получение списка всех доступных окон, начиная с времени запроса
    @GET("/appointments/free")
    suspend fun getFreeWindows(@Query("knoId") knoId: String): ListFreeWindows

    //запрос о записи от бизнеса
    @PUT("/business-user/appointments/select")
    suspend fun signUpToConsultation(@Body noteInfoForConsultationNetwork: NoteInfoForConsultationNetwork): ListFreeWindows

    //User
    @POST("user/register")
    suspend fun register(@Body newUser: UserRegisterInfoNetwork): IdNetwork

    @POST("/user/login")
    suspend fun login(@Body user: UserRegisterInfoNetwork): IdNetwork

    @POST("/user/role")
    suspend fun getRole(@Query("userId") userId: String): String
}