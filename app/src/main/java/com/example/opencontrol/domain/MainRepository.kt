package com.example.opencontrol.domain

import com.example.opencontrol.model.AnswerNetwork
import com.example.opencontrol.model.Note
import com.example.opencontrol.model.QuestionNetwork

interface MainRepository {
    fun getAllNotes(): List<Note>

    fun getNoteById(id: String): Note

    fun saveNote(note: Note): Boolean

    fun deleteNoteById(id: String): Boolean

    fun getDepartments(): List<String>

    fun getControlAgencies(): List<String>

    fun getControlTypes(): List<String>

    fun getFreeTimeForRecording(count: Int): List<String>

    fun getAnswerFromChat(question: QuestionNetwork): AnswerNetwork

}