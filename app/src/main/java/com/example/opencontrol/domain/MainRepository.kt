package com.example.opencontrol.domain

import com.example.opencontrol.model.networkDTOs.AnswerNetwork
import com.example.opencontrol.model.networkDTOs.ListKno
import com.example.opencontrol.model.networkDTOs.ListMeasures
import com.example.opencontrol.model.Note
import com.example.opencontrol.model.networkDTOs.IdNetwork
import com.example.opencontrol.model.networkDTOs.ListFreeWindows
import com.example.opencontrol.model.networkDTOs.QuestionNetwork
import com.example.opencontrol.model.networkDTOs.UserRegisterInfoNetwork
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getAllNotes(): List<Note>

    fun getNoteById(id: String): Note

    fun saveNote(note: Note): Boolean

    fun deleteNoteById(id: String): Boolean

    fun getDepartments(): List<String>

    fun getControlAgencies(): List<String>

    fun getControlTypes(): List<String>

    fun getFreeTimeForRecording(count: Int): List<String>

    fun getAnswerFromChat(question: QuestionNetwork): Flow<AnswerNetwork>

    fun getKnos(): Flow<ListKno>

    fun getMeasuresForKno(knoId: String): Flow<ListMeasures>

    fun getFreeWindows(knoId: String): Flow<ListFreeWindows>

    fun login(userRegisterInfoNetwork: UserRegisterInfoNetwork): Flow<IdNetwork>

    fun register(userRegisterInfoNetwork: UserRegisterInfoNetwork): Flow<IdNetwork>

}