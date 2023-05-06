package com.example.opencontrol.model

import java.time.LocalDate

data class Note(
    val id: String,
    val type: String,
    val time: String,
    val date: LocalDate,
    val inspector: String,
    val format:String,
    val objectNumber: Long,
    val info: String
)
