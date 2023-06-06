package com.example.opencontrol.model.networkDTOs

import com.google.gson.annotations.SerializedName

data class QuestionNetwork(
    val id: String,
    val question:String,
    @SerializedName("new_chat")
    val newChat: Boolean
)
