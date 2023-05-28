package com.example.opencontrol.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Kno(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class ListKno(
    @SerializedName("knoList")
    val knoList: List<Kno>
)
