package com.example.opencontrol.domain

import com.example.opencontrol.model.Note

interface MainRepository {
    fun getAllNotes(): List<Note>

    fun getNoteById(id: String): Note
}