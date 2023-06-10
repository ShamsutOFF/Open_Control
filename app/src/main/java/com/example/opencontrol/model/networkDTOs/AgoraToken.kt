package com.example.opencontrol.model.networkDTOs

import com.google.gson.annotations.SerializedName

data class AgoraToken(
    @SerializedName("rtcToken")
    val agoraToken: String
)
