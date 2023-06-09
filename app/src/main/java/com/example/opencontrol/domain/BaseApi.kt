package com.example.opencontrol.domain

import com.example.opencontrol.model.networkDTOs.AgreeNoteInfoNetwork
import com.example.opencontrol.model.networkDTOs.AppointmentId
import com.example.opencontrol.model.networkDTOs.BaseBusinessUserInfoNetwork
import com.example.opencontrol.model.networkDTOs.BaseInspectorUserInfoNetwork
import com.example.opencontrol.model.networkDTOs.BusinessUserInfoNetwork
import com.example.opencontrol.model.networkDTOs.IdNetwork
import com.example.opencontrol.model.networkDTOs.InspectorUserInfoNetwork
import com.example.opencontrol.model.networkDTOs.ListAppointments
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

    //СПРАВОЧНИКИ
    @GET("/info/knos")
    suspend fun getKnos(): ListKno

    @GET("/info/measures")
    suspend fun getMeasures(@Query("knoId") knoId: String): ListMeasures

    //ЗАПИСИ
    //получение списка всех доступных окон, начиная с времени запроса
    @GET("/appointments/free")
    suspend fun getFreeWindows(@Query("knoId") knoId: String): ListFreeWindows

    //отмена записи любой стороной
    @PUT("/appointments/cancel")
    suspend fun cancelConsultation(@Body appointmentId: AppointmentId)

    //получение списка всех записей бизнес-пользователя
    @GET("/business-user/appointments")
    suspend fun getAllBusinessAppointments(@Query("userId") userId: String): ListAppointments

    //запрос на запись от бизнеса
    @PUT("/business-user/appointments/select")
    suspend fun signUpToConsultation(@Body noteInfoForConsultationNetwork: NoteInfoForConsultationNetwork)

    //Добавление расширенной информации о пользователе
    @POST("/business-user/info")
    suspend fun addBusinessUserInfo(@Body userInfo: BusinessUserInfoNetwork)

    //Получение расширенной информации о пользователе
    @GET("/business-user/info")
    suspend fun getBusinessUserInfo(@Query("userId") userId: String): BaseBusinessUserInfoNetwork


    //User
    @POST("user/register")
    suspend fun register(@Body newUser: UserRegisterInfoNetwork): IdNetwork

    @POST("/user/login")
    suspend fun login(@Body user: UserRegisterInfoNetwork): IdNetwork


    //Добавление расширенной информации о инспекторе
    @POST("/inspection-user/info")
    suspend fun addInspectorUserInfo(@Body userInfo: InspectorUserInfoNetwork)

    //Получение расширенной информации о инспекторе
    @GET("/inspection-user/info")
    suspend fun getInspectorUserInfo(@Query("userId") userId: String): BaseInspectorUserInfoNetwork

    //получение списка всех записей инспектора-пользователя
    @GET("/inspection-user/appointments")
    suspend fun getAllInspectorAppointments(@Query("knoId") knoId: Int, @Query("inspectorId") inspectorId: String): ListAppointments

    //Подтверждение записи от инспекторе
    @PUT("/inspection-user/appointments/agree")
    suspend fun agreeAppointment(@Body agreeNoteInfoNetwork: AgreeNoteInfoNetwork)

    @POST("/user/role")
    suspend fun getRole(@Query("userId") userId: String): String
}