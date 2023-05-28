package com.example.opencontrol.model

import com.google.gson.annotations.SerializedName

data class Kno(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class ListKno(
    @SerializedName("knoList")
    val knoList: List<Kno>
)
