package com.example.opencontrol.model.networkDTOs

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.time.LocalDateTime

data class ListFreeWindows(
    @SerializedName("freeWindows")
    val freeWindows: List<FreeWindow>
)

data class FreeWindow(
    @SerializedName("id")
    val id: String,
    @SerializedName("appointmentTime")
    val appointmentTime: Timestamp
)

data class FreeWindowInLocalDateTime(
    val id: String,
    val appointmentTime: LocalDateTime
)
