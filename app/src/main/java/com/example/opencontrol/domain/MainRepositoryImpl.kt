package com.example.opencontrol.domain

import com.example.opencontrol.model.networkDTOs.ListKno
import com.example.opencontrol.model.networkDTOs.ListMeasures
import com.example.opencontrol.model.Note
import com.example.opencontrol.model.Person
import com.example.opencontrol.model.networkDTOs.AppointmentId
import com.example.opencontrol.model.networkDTOs.IdNetwork
import com.example.opencontrol.model.networkDTOs.ListAppointments
import com.example.opencontrol.model.networkDTOs.ListFreeWindows
import com.example.opencontrol.model.networkDTOs.NoteInfoForConsultationNetwork
import com.example.opencontrol.model.networkDTOs.QuestionNetwork
import com.example.opencontrol.model.networkDTOs.UserRegisterInfoNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.time.LocalDate
import java.util.UUID
import kotlin.random.Random

class MainRepositoryImpl(private val chatApi: ChatApi, private val baseApi: BaseApi) :
    MainRepository {

    override fun getAllNotes(): List<Note> {
        return notes
    }

    override fun getNoteById(id: String): Note {
        notes.forEach {
            if (it.id == id) {
                return it
            }
        }
        return emptyNote
    }

    override fun deleteNoteById(id: String): Boolean {
        return notes.remove(getNoteById(id))
    }

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

    private val firstNames =
        listOf("Александр", "Андрей", "Дмитрий", "Иван", "Максим", "Никита", "Сергей")
    private val lastNames =
        listOf("Иванов", "Петров", "Сидоров", "Смирнов", "Кузнецов", "Соколов", "Попов")
    private val patronymics = listOf(
        "Александрович",
        "Андреевич",
        "Дмитриевич",
        "Иванович",
        "Максимович",
        "Никитович",
        "Сергеевич"
    )

    private fun generateRandomPerson(): Person {
        return Person(firstNames.random(), lastNames.random(), patronymics.random())
    }

    private fun generateRandomDateWithinCurrentMonth(): LocalDate {
        val currentMonth = LocalDate.now().month
        val year = LocalDate.now().year
        val dayOfMonth = Random.nextInt(currentMonth.maxLength())
        return LocalDate.of(year, currentMonth, dayOfMonth + 1)
    }

    private fun generateRandomTime(): String {
        val startHour = 8
        val endHour = 19
        val durationMinutes = 30
        val startTime = startHour * 60
        val endTime = endHour * 60 - durationMinutes
        val startMinute = (startTime..endTime step durationMinutes).toList().random()
        val endMinute = startMinute + durationMinutes
        val startTimeString = "${startMinute / 60}.${String.format("%02d", startMinute % 60)}"
        val endTimeString = "${endMinute / 60}.${String.format("%02d", endMinute % 60)}"
        return "$startTimeString-$endTimeString"
    }

    private val emptyNote =
        Note(
            "",
            "empty",
            "empty",
            LocalDate.now(),
            Person("noName", "noName", "noName"),
            "empty",
            "empty",
            "empty"
        )

    private fun generateRandomNote(): Note {
        return Note(
            UUID.randomUUID().toString(),
            "проверка пожарной безопасности 1",
            generateRandomTime(),
            generateRandomDateWithinCurrentMonth(),
            generateRandomPerson(),
            "выездная проверка",
            Random.nextInt(10000, 99999).toString(),
            "Подготовить паспорт объекта"
        )
    }

    private var notes = mutableListOf(
        generateRandomNote(),
        generateRandomNote(),
        generateRandomNote(),
        generateRandomNote(),
        generateRandomNote(),
        generateRandomNote(),
        generateRandomNote(),
        generateRandomNote(),
        generateRandomNote(),
        generateRandomNote(),
    )
}