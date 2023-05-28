package com.example.opencontrol.model

import com.google.gson.annotations.SerializedName

data class ListMeasures(
    @SerializedName("measures")
    val measuresList: List<Measures>
)

data class Measures(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
