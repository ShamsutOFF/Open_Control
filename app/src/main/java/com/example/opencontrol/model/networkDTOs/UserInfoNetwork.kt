package com.example.opencontrol.model.networkDTOs

import com.google.gson.annotations.SerializedName

data class UserInfoNetwork(
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("surName")
    val surName: String?,
    @SerializedName("inn")
    val inn: Long?,
    @SerializedName("snils")
    val snils: Long?
)
data class BaseUserInfoNetwork (
    @SerializedName("user") val user: UserInfoNetwork
)
