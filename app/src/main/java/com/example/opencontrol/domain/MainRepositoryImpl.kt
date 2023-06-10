package com.example.opencontrol.domain

import com.example.opencontrol.model.networkDTOs.AgoraToken
import com.example.opencontrol.model.networkDTOs.AgreeNoteInfoNetwork
import com.example.opencontrol.model.networkDTOs.AppointmentId
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
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class MainRepositoryImpl(private val chatApi: ChatApi, private val baseApi: BaseApi, private val agoraApi: AgoraApi) :
    MainRepository {
    override fun getAnswerFromChat(question: QuestionNetwork) =
        flow {
            Timber.d("@@@ getAnswerFromChat question = $question")
            emit(chatApi.getAnswerFromBot(question))
        }

    override fun getKnos(): Flow<ListKno> =
        flow {
            emit(baseApi.getKnos())
        }

    override fun getMeasuresForKno(knoId: String): Flow<ListMeasures> =
        flow {
            emit(baseApi.getMeasures(knoId))
        }

    override fun getFreeWindows(knoId: String): Flow<ListFreeWindows> =
        flow {
            emit(baseApi.getFreeWindows(knoId))
        }

    override fun login(userRegisterInfoNetwork: UserRegisterInfoNetwork): Flow<IdNetwork> =
        flow {
            Timber.d("@@@ login userRegisterInfoNetwork = $userRegisterInfoNetwork")
            emit(baseApi.login(userRegisterInfoNetwork))
        }

    override fun register(userRegisterInfoNetwork: UserRegisterInfoNetwork): Flow<IdNetwork> =
        flow {
            Timber.d("@@@ login userRegisterInfoNetwork = $userRegisterInfoNetwork")
            emit(baseApi.register(userRegisterInfoNetwork))
        }

    override fun noteMeToConsultation(noteInfoForConsultationNetwork: NoteInfoForConsultationNetwork): Flow<Unit> =
        flow {
            Timber.d("@@@ login noteInfoForConsultationNetwork = $noteInfoForConsultationNetwork")
            emit(baseApi.signUpToConsultation(noteInfoForConsultationNetwork))
        }

    override fun getAllBusinessAppointments(userId: String): Flow<ListAppointments> =
        flow {
            emit(baseApi.getAllBusinessAppointments(userId))
        }

    override fun cancelAppointment(appointmentId: String): Flow<Unit> =
        flow {
            emit(baseApi.cancelConsultation(AppointmentId(appointmentId)))
        }


    override fun saveBusinessUserInfo(businessUserInfoNetwork: BusinessUserInfoNetwork): Flow<Unit> =
        flow {
            emit(baseApi.addBusinessUserInfo(businessUserInfoNetwork))
        }

    override fun getBusinessUserInfo(userId: String): Flow<BaseBusinessUserInfoNetwork> =
        flow {
            emit(baseApi.getBusinessUserInfo(userId))
        }

    override fun saveInspectorUserInfo(inspectorUserInfoNetwork: InspectorUserInfoNetwork): Flow<Unit> =
        flow{
        emit(baseApi.addInspectorUserInfo(inspectorUserInfoNetwork))
    }

    override fun getInspectorUserInfo(userId: String): Flow<BaseInspectorUserInfoNetwork> =
        flow{
        emit(baseApi.getInspectorUserInfo(userId))
    }

    override fun getAllInspectorAppointments(
        inspectorKnoId: Int,
        userId: String
    ): Flow<ListAppointments> = flow{
        emit(baseApi.getAllInspectorAppointments(inspectorKnoId,userId))
    }

    override fun agreeAppointment(agreeNoteInfoNetwork: AgreeNoteInfoNetwork): Flow<Unit> = flow{
        emit(baseApi.agreeAppointment(agreeNoteInfoNetwork))
    }

    override fun getAgoraToken(userId: String): Flow<AgoraToken> = flow{
        emit(agoraApi.getAgoraToken(userId))
    }
}