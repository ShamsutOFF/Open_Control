package com.example.opencontrol.domain

import com.example.opencontrol.model.networkDTOs.AgreeNoteInfoNetwork
import com.example.opencontrol.model.networkDTOs.AnswerNetwork
import com.example.opencontrol.model.networkDTOs.BaseBusinessUserInfoNetwork
import com.example.opencontrol.model.networkDTOs.BaseInspectorUserInfoNetwork
import com.example.opencontrol.model.networkDTOs.IdNetwork
import com.example.opencontrol.model.networkDTOs.ListAppointments
import com.example.opencontrol.model.networkDTOs.ListFreeWindows
import com.example.opencontrol.model.networkDTOs.ListKno
import com.example.opencontrol.model.networkDTOs.ListMeasures
import com.example.opencontrol.model.networkDTOs.NoteInfoForConsultationNetwork
import com.example.opencontrol.model.networkDTOs.QuestionNetwork
import com.example.opencontrol.model.networkDTOs.BusinessUserInfoNetwork
import com.example.opencontrol.model.networkDTOs.InspectorUserInfoNetwork
import com.example.opencontrol.model.networkDTOs.UserRegisterInfoNetwork
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getAnswerFromChat(question: QuestionNetwork): Flow<AnswerNetwork>

    fun getKnos(): Flow<ListKno>

    fun getMeasuresForKno(knoId: String): Flow<ListMeasures>

    fun getFreeWindows(knoId: String): Flow<ListFreeWindows>

    fun login(userRegisterInfoNetwork: UserRegisterInfoNetwork): Flow<IdNetwork>

    fun register(userRegisterInfoNetwork: UserRegisterInfoNetwork): Flow<IdNetwork>

    fun noteMeToConsultation(noteInfoForConsultationNetwork: NoteInfoForConsultationNetwork): Flow<Unit>

    fun getAllBusinessAppointments(userId: String): Flow<ListAppointments>

    fun cancelAppointment(appointmentId: String): Flow<Unit>

    fun saveBusinessUserInfo(businessUserInfoNetwork: BusinessUserInfoNetwork): Flow<Unit>

    fun getBusinessUserInfo(userId: String): Flow<BaseBusinessUserInfoNetwork>

    fun saveInspectorUserInfo(inspectorUserInfoNetwork: InspectorUserInfoNetwork): Flow<Unit>

    fun getInspectorUserInfo(userId: String): Flow<BaseInspectorUserInfoNetwork>

    fun getAllInspectorAppointments(inspectorKnoId: Int, userId: String): Flow<ListAppointments>

    fun agreeAppointment( agreeNoteInfoNetwork: AgreeNoteInfoNetwork): Flow<Unit>
}