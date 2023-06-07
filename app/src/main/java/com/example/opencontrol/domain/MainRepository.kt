package com.example.opencontrol.domain

import com.example.opencontrol.model.networkDTOs.AnswerNetwork
import com.example.opencontrol.model.networkDTOs.ListKno
import com.example.opencontrol.model.networkDTOs.ListMeasures
import com.example.opencontrol.model.Note
import com.example.opencontrol.model.networkDTOs.IdNetwork
import com.example.opencontrol.model.networkDTOs.ListAppointments
import com.example.opencontrol.model.networkDTOs.ListFreeWindows
import com.example.opencontrol.model.networkDTOs.NoteInfoForConsultationNetwork
import com.example.opencontrol.model.networkDTOs.QuestionNetwork
import com.example.opencontrol.model.networkDTOs.UserRegisterInfoNetwork
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getAllNotes(): List<Note>

    fun getNoteById(id: String): Note

    fun deleteNoteById(id: String): Boolean

    fun getAnswerFromChat(question: QuestionNetwork): Flow<AnswerNetwork>

    fun getKnos(): Flow<ListKno>

    fun getMeasuresForKno(knoId: String): Flow<ListMeasures>

    fun getFreeWindows(knoId: String): Flow<ListFreeWindows>

    fun login(userRegisterInfoNetwork: UserRegisterInfoNetwork): Flow<IdNetwork>

    fun register(userRegisterInfoNetwork: UserRegisterInfoNetwork): Flow<IdNetwork>

    fun noteMeToConsultation(noteInfoForConsultationNetwork: NoteInfoForConsultationNetwork): Flow<Unit>

    fun getAllAppointments(userId: String): Flow<ListAppointments>

    fun cancelAppointment(appointmentId: String): Flow<Unit>
}