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
import com.example.opencontrol.model.networkDTOs.Appointments
import com.example.opencontrol.model.networkDTOs.AppointmentsInLocalDateTime
import com.example.opencontrol.model.networkDTOs.FreeWindow
import com.example.opencontrol.model.networkDTOs.FreeWindowInLocalDateTime
import com.example.opencontrol.model.networkDTOs.NoteInfoForConsultationNetwork
import com.example.opencontrol.model.networkDTOs.QuestionNetwork
import com.example.opencontrol.model.networkDTOs.UserRegisterInfoNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
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
    var businessAppointments = mutableStateListOf<Appointments>()

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

    fun getNearestAppointment(): AppointmentsInLocalDateTime? {
        val notes = getAllBusinessAppointmentsInLDT().sortedBy { it.time }
        val nearestIndex = notes.indexOfFirst { it.time >= LocalDateTime.now() }
        return if (nearestIndex != -1)
            notes[nearestIndex]
        else
            null
    }

    fun getAppointmentById(id: String): AppointmentsInLocalDateTime {
        return getAllBusinessAppointmentsInLDT().first {
            it.id == id
        }
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

    fun getAllBusinessAppointmentsInLDT(): List<AppointmentsInLocalDateTime> {
        Timber.d("@@@ getAllBusinessAppointmentsInLDT businessAppointments = ${businessAppointments.toList()}")
        val appointmentsInLocalDateTime = businessAppointments.toList().map {
            AppointmentsInLocalDateTime(
                id = it.id,
                time = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(it.time.time),
                    ZoneId.systemDefault()
                ),
                status = it.status,
                knoId = it.knoId,
                knoName = it.knoName,
                measureId = it.measureId,
                measureName = it.measureName
            )
        }
        return appointmentsInLocalDateTime
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

    fun noteMeToConsultation(appointmentId: String, selectedMeasure: String) {
        Timber.d("@@@ noteMeToConsultation(appointmentId = $appointmentId, selectedMeasure = $selectedMeasure")
        viewModelScope.launch {
            measuresForKno.toList().find {
                it.name == selectedMeasure
            }?.let {
                repository.noteMeToConsultation(
                    NoteInfoForConsultationNetwork(
                        userId = userId,
                        appointmentId = appointmentId,
                        measureId = it.id
                    )
                )
                    .flowOn(Dispatchers.IO)
                    .catch { ex ->
                        Timber.e(ex)
                    }
                    .collect {
                    }
            }
        }
    }

    fun getAllAppointments() {
        Timber.d("@@@ getAllAppointments()")
        viewModelScope.launch {
            repository.getAllAppointments(userId)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    businessAppointments.addAll(it.appointments)
                }
        }
    }

    fun cancelConsultation(appointmentId: String) {
        Timber.d("@@@ cancelConsultation(appointmentId = $appointmentId)")
        viewModelScope.launch {
            repository.cancelAppointment(appointmentId)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {

                }
        }
    }
}