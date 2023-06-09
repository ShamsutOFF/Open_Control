package com.example.opencontrol.model.networkDTOs

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.time.LocalDateTime

data class ListAppointments(
    @SerializedName("appointments")
    val appointments : List<Appointments>
)
data class Appointments (
    @SerializedName("id")
    val id : String,
    @SerializedName("time")
    val time : Timestamp,
    @SerializedName("businessUserId")
    val businessUserId : String?,
    @SerializedName("status")
    val status : String,
    @SerializedName("knoId")
    val knoId : Int,
    @SerializedName("knoName")
    val knoName : String?,
    @SerializedName("measureId")
    val measureId : Int,
    @SerializedName("measureName")
    val measureName : String?
)

data class AppointmentsInLocalDateTime (
    val id : String,
    val time : LocalDateTime,
    val status : String,
    val businessUserId : String,
    val knoId : Int,
    val knoName : String,
    val measureId : Int,
    val measureName : String
)
data class AppointmentId (
    @SerializedName("appointmentId")
    val id : String
)