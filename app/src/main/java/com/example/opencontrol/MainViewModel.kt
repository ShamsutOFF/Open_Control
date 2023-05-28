package com.example.opencontrol

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opencontrol.domain.MainRepository
import com.example.opencontrol.model.AnswerNetwork
import com.example.opencontrol.model.ChatMessage
import com.example.opencontrol.model.Kno
import com.example.opencontrol.model.Note
import com.example.opencontrol.model.QuestionNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    var selectedDate: LocalDate by mutableStateOf(LocalDate.now())
        private set

    var photoUris = mutableStateListOf<Uri>()

    var chatListOfMessages = mutableStateListOf<ChatMessage>()

    init {
        Timber.d("@@@ init")
        chatListOfMessages.add(ChatMessage("Здравствуйте! Я бот-помощник. Чем могу помочь?", false))
        getKnos()
    }

    fun getKnos() {
        Timber.d("@@@ getKnos")
        viewModelScope.launch {
            repository.getKnos()
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    Timber.d("@@@ List KNO = $it")
                }
        }
    }

    fun getAnswerFromChat(question: String) {
        val questionNetwork = QuestionNetwork(id = 12345, question = question, newChat = false)
        viewModelScope.launch {
            repository.getAnswerFromChat(questionNetwork)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    chatListOfMessages.add(ChatMessage(it.answer, false))
                }
        }
    }

    fun getAllNotes(): List<Note> {
        return repository.getAllNotes()
    }

    fun getNearestNote(): Note? {
        val notes = repository.getAllNotes().sortedBy { it.date }
        val nearestIndex = notes.indexOfFirst { it.date >= LocalDate.now() }
        return if (nearestIndex != -1)
            notes[nearestIndex]
        else
            null
    }

    fun getNoteById(id: String): Note {
        return repository.getNoteById(id)
    }

    fun getControlAgencies(): List<String> {
        return repository.getControlAgencies()
    }

    fun getDepartments(): List<String> {
        return repository.getDepartments()
    }

    fun getControlTypes(): List<String> {
        return repository.getControlTypes()
    }

    fun getFreeTimeForRecording(count: Int): List<String> {
        return repository.getFreeTimeForRecording(count)
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