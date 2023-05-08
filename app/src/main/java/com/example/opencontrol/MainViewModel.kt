package com.example.opencontrol

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.opencontrol.domain.MainRepository
import com.example.opencontrol.model.Note
import java.time.LocalDate
import java.util.Random

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val viewModelId = Random().nextInt(1000)
    var selectedDate by mutableStateOf(LocalDate.now())
        private set

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllNotes(): List<Note> {
        return repository.getAllNotes()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNoteById(id: String): Note {
        return repository.getNoteById(id)
    }

    fun changeSelectedDate(newDate: LocalDate){
        selectedDate = newDate
    }
}