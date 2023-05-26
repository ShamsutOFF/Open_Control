package com.example.opencontrol.model

import com.google.gson.annotations.SerializedName

data class QuestionNetwork(
    val id: Int,
    val question:String,
    @SerializedName("new_chat")
    val newChat: Boolean
)
