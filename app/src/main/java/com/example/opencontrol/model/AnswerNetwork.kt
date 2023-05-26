package com.example.opencontrol.model

import com.google.gson.annotations.SerializedName

data class AnswerNetwork(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("success")
    val success: Boolean
)
