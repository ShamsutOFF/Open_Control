package com.example.opencontrol.model.networkDTOs

import com.google.gson.annotations.SerializedName

data class NoteInfoForConsultationNetwork(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("appointmentId")
    val appointmentId: String,
    @SerializedName("measureId")
    val measureId: Int
)
