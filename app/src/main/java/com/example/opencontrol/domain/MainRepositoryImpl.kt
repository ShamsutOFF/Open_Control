package com.example.opencontrol.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.opencontrol.model.Note
import java.time.LocalDate
import java.util.UUID
import kotlin.random.Random

class MainRepositoryImpl(private val api: MyApi) : MainRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAllNotes(): List<Note> {
        return notes
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getNoteById(id: String): Note {
        notes.forEach {
            if (it.id == id) {
                return it
            }
        }
        return emptyNote
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun saveNote(note: Note): Boolean {
        return notes.add(note)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deleteNoteById(id: String): Boolean {
        return notes.remove(getNoteById(id))
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

    private fun generateRandomFIO(): String {
        val firstName = firstNames.random()
        val lastName = lastNames.random()
        val patronymic = patronymics.random()
        return "$lastName $firstName $patronymic"
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        val startMinute = Random.nextInt(startTime, endTime)
        val endMinute = startMinute + durationMinutes
        val startTimeString = "${startMinute / 60}.${String.format("%02d", startMinute % 60)}"
        val endTimeString = "${endMinute / 60}.${String.format("%02d", endMinute % 60)}"
        return "$startTimeString-$endTimeString"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val emptyNote =
        Note(
            "",
            "empty",
            "empty",
            LocalDate.now(),
            "empty",
            "empty",
            "empty",
            "empty"
        )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateRandomNote(): Note {
        return Note(
            UUID.randomUUID().toString(),
            "проверка пожарной безопасности 1",
            generateRandomTime(),
            generateRandomDateWithinCurrentMonth(),
            generateRandomFIO(),
            "выездная проверка",
            Random.nextInt(10000, 99999).toString(),
            "Подготовить паспорт объекта"
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
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