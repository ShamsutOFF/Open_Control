package com.example.opencontrol

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.opencontrol.domain.MainRepository
import com.example.opencontrol.model.ChatMessage
import com.example.opencontrol.model.UserRole
import com.example.opencontrol.model.networkDTOs.Kno
import com.example.opencontrol.model.database.KnoDao
import com.example.opencontrol.model.networkDTOs.AgreeNoteInfoNetwork
import com.example.opencontrol.model.networkDTOs.Measures
import com.example.opencontrol.model.networkDTOs.Appointments
import com.example.opencontrol.model.networkDTOs.AppointmentsInLocalDateTime
import com.example.opencontrol.model.networkDTOs.FreeWindow
import com.example.opencontrol.model.networkDTOs.FreeWindowInLocalDateTime
import com.example.opencontrol.model.networkDTOs.NoteInfoForConsultationNetwork
import com.example.opencontrol.model.networkDTOs.QuestionNetwork
import com.example.opencontrol.model.networkDTOs.BusinessUserInfoNetwork
import com.example.opencontrol.model.networkDTOs.InspectorUserInfoNetwork
import com.example.opencontrol.model.networkDTOs.UserRegisterInfoNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class MainViewModel(
    context: Application,
    private val repository: MainRepository,
    private val knoDao: KnoDao
) : AndroidViewModel(context) {
    companion object {
        val USERID_TAG = "user_id"
        val START_MESSAGE = ChatMessage(
            "Здравствуйте! Я бот-помощник. Чем могу помочь?",
            false
        )
    }

    var userId by mutableStateOf("")
    var userRole by mutableStateOf(UserRole.BUSINESS)
    var inspectorKnoId by mutableStateOf(0)
    var businessUserInfo by mutableStateOf(BusinessUserInfoNetwork(userId, "", "", "", "", 0L, 0L))
        private set
    var inspectorUserInfo by mutableStateOf(
        InspectorUserInfoNetwork(
            userId,
            "",
            "",
            "",
            "",
            inspectorKnoId
        )
    )
        private set

    var selectedDate: LocalDate by mutableStateOf(LocalDate.now())
        private set

    var photoUris = mutableStateListOf<Uri>()

    var chatListOfMessages = mutableStateListOf<ChatMessage>()
    var listOfAllKnos = mutableStateListOf<Kno>()
    var measuresForKno = mutableStateListOf<Measures>()
    var freeWindows = mutableStateListOf<FreeWindow>()
    var businessAppointments = mutableStateListOf<Appointments>()
    var inspectorAppointments = mutableStateListOf<Appointments>()

    //save creds
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val sharedPreferencesEditor: SharedPreferences.Editor = sharedPreferences.edit()

    init {
        Timber.d("@@@ init")
        chatListOfMessages.add(START_MESSAGE)
        downloadKnosToDatabase()
        getKnosFromRoom()
    }

    fun getAllBusinessAppointments() {
        Timber.d("@@@ getAllAppointments()")
        viewModelScope.launch {
            repository.getAllBusinessAppointments(userId)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    businessAppointments.clear()
                    businessAppointments.addAll(it.appointments)
                }
        }
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
                knoName = it.knoName ?: "",
                businessUserId = it.businessUserId ?: "",
                measureId = it.measureId,
                measureName = it.measureName ?: ""
            )
        }
        return appointmentsInLocalDateTime
    }

    suspend fun getKnoByName(name: String): Kno? {
        return knoDao.getKnoByName(name)
    }

    suspend fun getKnoById(knoId: Int): Kno? {
        Timber.d("@@@ getKnoById(knoId = $knoId)")
        Timber.d("@@@ knoDao.getKnoById(knoId) = ${knoDao.getKnoById(knoId)}")
        return knoDao.getKnoById(knoId)
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

    fun completeBotConsultation() {
        chatListOfMessages.clear()
        chatListOfMessages.add(START_MESSAGE)
    }

    fun getNearestAppointment(): AppointmentsInLocalDateTime? {
        Timber.d("@@@ getNearestAppointment()")
        val notes = getAllBusinessAppointmentsInLDT().sortedBy { it.time }
        val nearestIndex = notes.indexOfFirst { it.time >= LocalDateTime.now() }
        return if (nearestIndex != -1)
            notes[nearestIndex]
        else
            null
    }

    fun getAppointmentById(id: String): AppointmentsInLocalDateTime? {
        Timber.d("@@@ getAppointmentById(id = $id)")
        if (userRole == UserRole.BUSINESS)
            return getAllBusinessAppointmentsInLDT().firstOrNull() {
                it.id == id
            }
        else return getAllInspectorAppointmentsInLDT().firstOrNull() {
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
                    sharedPreferencesEditor.putString(USERID_TAG, userId)
                }
        }
    }

    fun loggedIn() = sharedPreferences.contains(USERID_TAG).also {
        if (it) userId = sharedPreferences.getString(USERID_TAG, null)!!
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
                    sharedPreferencesEditor.putString(USERID_TAG, userId)
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

    fun saveBusinessUserInfo(businessUserInfoNetwork: BusinessUserInfoNetwork) {
        Timber.d("@@@ saveUserInfo(businessUserInfoNetwork = $businessUserInfoNetwork)")
        viewModelScope.launch {
            repository.saveBusinessUserInfo(businessUserInfoNetwork)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {

                }
        }
    }

    fun getBusinessUserInfo() {
        Timber.d("@@@ getBusinessUserInfo()")
        viewModelScope.launch {
            repository.getBusinessUserInfo(userId)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    Timber.d("@@@ getBusinessUserInfo() it = $it")
                    Timber.d("@@@ getBusinessUserInfo() it.user = ${it.user}")
                    businessUserInfo = it.user
                    Timber.d("@@@ getBusinessUserInfo() userInfo = $businessUserInfo")
                }
        }
        Timber.d("@@@ 2getUserInfo() userInfo = $businessUserInfo")
    }

    fun getBusinessUserInfoFromInspector(businessUserId: String): StateFlow<BusinessUserInfoNetwork?> {
        Timber.d("@@@ getBusinessUserInfoFromInspector(businessUserId = $businessUserId)")
        val businessUserInfoFlow = MutableStateFlow<BusinessUserInfoNetwork?>(null)
        viewModelScope.launch {
            repository.getBusinessUserInfo(businessUserId)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    Timber.d("@@@ getBusinessUserInfoFromInspector it.user = ${it.user})")
                    businessUserInfoFlow.value = it.user
                }
        }
        return businessUserInfoFlow
    }

    fun clearUser() {
        userId = ""
        businessUserInfo = BusinessUserInfoNetwork(userId, "", "", "", "", 0L, 0L)
        inspectorKnoId = 0
    }

    fun saveInspectorUserInfo(inspectorUserInfoNetwork: InspectorUserInfoNetwork) {
        Timber.d("@@@ saveInspectorUserInfo(inspectorUserInfoNetwork = $inspectorUserInfoNetwork) userInfo = $businessUserInfo")
        Timber.d("@@@ saveInspectorUserInfo userInfo = $businessUserInfo")
        Timber.d("@@@ saveInspectorUserInfo inspectorKnoId = $inspectorKnoId")
        viewModelScope.launch {
            repository.saveInspectorUserInfo(inspectorUserInfoNetwork)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {

                }
        }
    }

    fun getInspectorUserInfo() {
        Timber.d("@@@ getInspectorUserInfo()")
        viewModelScope.launch {
            repository.getInspectorUserInfo(userId)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    inspectorUserInfo = it.user
                    inspectorKnoId = it.user.knoId ?: 0
                }
        }
    }

    fun getAllInspectorAppointments() {
        Timber.d("@@@@@ getAllInspectorAppointments()")
        viewModelScope.launch {
            repository.getAllInspectorAppointments(inspectorKnoId, userId)
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {
                    inspectorAppointments.clear()
                    inspectorAppointments.addAll(it.appointments)
                }
        }
    }

    fun getAllInspectorAppointmentsInLDT(): List<AppointmentsInLocalDateTime> {
        Timber.d("@@@ getAllInspectorAppointmentsInLDT inspectorAppointments = ${inspectorAppointments.toList()}")
        val appointmentsInLocalDateTime = inspectorAppointments.toList().map {
            AppointmentsInLocalDateTime(
                id = it.id,
                time = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(it.time.time),
                    ZoneId.systemDefault()
                ),
                status = it.status,
                knoId = it.knoId,
                knoName = it.knoName ?: "",
                businessUserId = it.businessUserId ?: "",
                measureId = it.measureId,
                measureName = it.measureName ?: ""
            )
        }
        return appointmentsInLocalDateTime
    }

    fun agreeAppointment(appointmentId: String) {
        Timber.d("@@@@@ getAllInspectorAppointments()")
        viewModelScope.launch {
            repository.agreeAppointment(AgreeNoteInfoNetwork(userId, appointmentId))
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    Timber.e(ex)
                }
                .collect {

                }
        }
    }
}