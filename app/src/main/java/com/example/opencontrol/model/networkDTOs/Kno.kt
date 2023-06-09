package com.example.opencontrol.model.networkDTOs

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ListKno(
    @SerializedName("knoList")
    val knoList: List<Kno>
)

@Entity
data class Kno(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
