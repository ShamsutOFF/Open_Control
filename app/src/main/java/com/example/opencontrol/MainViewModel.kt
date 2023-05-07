package com.example.opencontrol

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.opencontrol.domain.MainRepository
import com.example.opencontrol.model.Note
import java.util.Random

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val viewModelId = Random().nextInt(1000)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllNotes(): List<Note> {
        return repository.getAllNotes()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNoteById(id: String): Note {
        return repository.getNoteById(id)
    }
}