package com.example.opencontrol.model.networkDTOs

import com.google.gson.annotations.SerializedName

data class AgreeNoteInfoNetwork(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("appointmentId")
    val appointmentId: String
)
