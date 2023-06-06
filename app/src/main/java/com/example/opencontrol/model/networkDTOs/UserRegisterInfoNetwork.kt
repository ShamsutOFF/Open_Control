package com.example.opencontrol.model.networkDTOs

import com.google.gson.annotations.SerializedName

data class UserRegisterInfoNetwork(
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("role")
    val role: String
)