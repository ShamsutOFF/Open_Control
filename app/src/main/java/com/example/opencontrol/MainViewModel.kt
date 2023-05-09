package com.example.opencontrol

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.opencontrol.domain.MainRepository
import com.example.opencontrol.model.Note
import java.time.LocalDate
import java.util.Random

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    val viewModelId = Random().nextInt(1000)
    var selectedDate: LocalDate by mutableStateOf(LocalDate.now())
        private set

    fun getAllNotes(): List<Note> {
        return repository.getAllNotes()
    }

    fun getNoteById(id: String): Note {
        return repository.getNoteById(id)
    }

    fun addNewNote(note: Note): Boolean {
        return repository.saveNote(note)
    }

    fun deleteNoteById(id: String): Boolean {
        return repository.deleteNoteById(id)
    }

    fun changeSelectedDate(newDate: LocalDate) {
        selectedDate = newDate
    }
}