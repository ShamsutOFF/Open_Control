package com.example.opencontrol.domain

import com.example.opencontrol.model.networkDTOs.AppointmentId
import com.example.opencontrol.model.networkDTOs.BaseUserInfoNetwork
import com.example.opencontrol.model.networkDTOs.IdNetwork
import com.example.opencontrol.model.networkDTOs.ListAppointments
import com.example.opencontrol.model.networkDTOs.ListFreeWindows
import com.example.opencontrol.model.networkDTOs.ListKno
import com.example.opencontrol.model.networkDTOs.ListMeasures
import com.example.opencontrol.model.networkDTOs.NoteInfoForConsultationNetwork
import com.example.opencontrol.model.networkDTOs.QuestionNetwork
import com.example.opencontrol.model.networkDTOs.UserInfoNetwork
import com.example.opencontrol.model.networkDTOs.UserRegisterInfoNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class MainRepositoryImpl(private val chatApi: ChatApi, private val baseApi: BaseApi) :
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

    override fun getAllAppointments(userId: String): Flow<ListAppointments> =
        flow {
            emit(baseApi.getAllBusinessAppointments(userId))
        }

    override fun cancelAppointment(appointmentId: String): Flow<Unit> =
        flow {
            emit(baseApi.cancelConsultation(AppointmentId(appointmentId)))
        }

    override fun saveUserInfo(userInfoNetwork: UserInfoNetwork): Flow<Unit> =
        flow {
            emit(baseApi.addUserInfo(userInfoNetwork))
        }

    override fun getUserInfo(userId: String): Flow<BaseUserInfoNetwork> =
        flow {
            emit(baseApi.getUserInfo(userId))
        }
}