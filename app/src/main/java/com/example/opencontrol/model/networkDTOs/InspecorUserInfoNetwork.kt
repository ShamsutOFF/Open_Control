package com.example.opencontrol.model.networkDTOs

import com.google.gson.annotations.SerializedName

data class InspectorUserInfoNetwork(
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
    @SerializedName("knoId")
    val knoId: Int?
)
data class BaseInspectorUserInfoNetwork (
    @SerializedName("user") val user: InspectorUserInfoNetwork
)
