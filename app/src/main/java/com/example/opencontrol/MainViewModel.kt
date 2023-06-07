package com.example.opencontrol

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opencontrol.domain.MainRepository
import com.example.opencontrol.model.ChatMessage
import com.example.opencontrol.model.networkDTOs.Kno
import com.example.opencontrol.model.database.KnoDao
import com.example.opencontrol.model.networkDTOs.Measures
import com.example.opencontrol.model.Note
import com.example.opencontrol.model.networkDTOs.FreeWindow
import com.example.opencontrol.model.networkDTOs.FreeWindowInLocalDateTime
import com.example.opencontrol.model.networkDTOs.QuestionNetwork
import com.example.opencontrol.model.networkDTOs.UserRegisterInfoNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class MainViewModel(
    private val repository: MainRepository,
    private val knoDao: KnoDao
) : ViewModel() {
    var userId by mutableStateOf("")

    var selectedDate: LocalDate by mutableStateOf(LocalDate.now())
        private set

    var photoUris = mutableStateListOf<Uri>()

    var chatListOfMessages = mutableStateListOf<ChatMessage>()
    var listOfAllKnos = mutableStateListOf<Kno>()
    var measuresForKno = mutableStateListOf<Measures>()
    var freeWindows = mutableStateListOf<FreeWindow>()

    init {
        Timber.d("@@@ init")
        chatListOfMessages.add(ChatMessage("Здравствуйте! Я бот-помощник. Чем могу помочь?", false))
        downloadKnosToDatabase()
        getKnosFromRoom()
    }

    suspend fun getKnoByName(name: String): Kno? {
        return knoDao.getKnoByName(name)
    }

    fun getFreeWindows(knoId: String) {
        viewModelScope.launch {
            repository.getFreeWindows(knoId)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    freeWindows.clear()
                    freeWindows.addAll(it.freeWindows)
                }
        }

    }

    private fun downloadKnosToDatabase() {
        Timber.d("@@@ getKnos")
        viewModelScope.launch {
            repository.getKnos()
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    listOfAllKnos.addAll(it.knoList)
                    knoDao.insertAllKno(it.knoList)
                }
        }
    }

    private fun getKnosFromRoom() {
        viewModelScope.launch {
            knoDao.getAllKno()
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    listOfAllKnos.addAll(it)
                }
        }
    }

    fun getMeasuresForKno(knoId: String) {
        viewModelScope.launch {
            repository.getMeasuresForKno(knoId)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    measuresForKno.clear()
                    measuresForKno.addAll(it.measuresList)
                }
        }

    }

    fun getAnswerFromChat(question: String) {
        val questionNetwork = QuestionNetwork(id = userId, question = question, newChat = false)
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

    fun getFreeTimeForSelectedDate(): List<FreeWindowInLocalDateTime> {
        Timber.d("@@@ freeWindows = $freeWindows")
        val freeWindowsForSelectedDate = freeWindows.toList().filter {
            val date = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(it.appointmentTime.time),
                ZoneId.systemDefault()
            )
            date.toLocalDate() == selectedDate
        }.map {
            FreeWindowInLocalDateTime(
                it.id,
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(it.appointmentTime.time),
                    ZoneId.systemDefault()
                )
            )
        }
        return freeWindowsForSelectedDate
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

    fun login(userRegisterInfoNetwork: UserRegisterInfoNetwork) {
        viewModelScope.launch {
            repository.login(userRegisterInfoNetwork)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    userId = it.id
                }
        }
    }

    fun registerNewAccount(userRegisterInfoNetwork: UserRegisterInfoNetwork) {
        viewModelScope.launch {
            repository.register(userRegisterInfoNetwork)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    userId = it.id
                }
        }
    }

}