package com.example.opencontrol.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.opencontrol.model.Note
import java.time.LocalDate
import java.util.UUID
import kotlin.random.Random

class MainRepositoryImpl( private val api: MyApi) : MainRepository {

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

    private val firstNames = listOf("Александр", "Андрей", "Дмитрий", "Иван", "Максим", "Никита", "Сергей")
    private val lastNames = listOf("Иванов", "Петров", "Сидоров", "Смирнов", "Кузнецов", "Соколов", "Попов")
    private val patronymics = listOf("Александрович", "Андреевич", "Дмитриевич", "Иванович", "Максимович", "Никитович", "Сергеевич")

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

    @RequiresApi(Build.VERSION_CODES.O)
    private val emptyNote =
        Note("",
            "empty",
            "empty",
            LocalDate.now(),
            "empty",
            "empty",
            "empty",
            "empty")

    @RequiresApi(Build.VERSION_CODES.O)
    private val notes = listOf(
        Note(
            UUID.randomUUID().toString(),
            "проверка пожарной безопасности 1",
            "8:30-9:00",
            generateRandomDateWithinCurrentMonth(),
            generateRandomFIO(),
            "выездная проверка",
            Random.nextInt(10000,99999).toString(),
            "Подготовить паспорт объекта"
        ),
        Note(
            UUID.randomUUID().toString(),
            "проверка пожарной безопасности 2",
            "8:30-9:00",
            generateRandomDateWithinCurrentMonth(),
            generateRandomFIO(),
            "выездная проверка",
            Random.nextInt(10000,99999).toString(),
            "Подготовить паспорт объекта"
        ),
        Note(
            UUID.randomUUID().toString(),
            "проверка пожарной безопасности 3",
            "8:30-9:00",
            generateRandomDateWithinCurrentMonth(),
            generateRandomFIO(),
            "выездная проверка",
            Random.nextInt(10000,99999).toString(),
            "Подготовить паспорт объекта"
        ),
        Note(
            UUID.randomUUID().toString(),
            "проверка пожарной безопасности 4",
            "8:30-9:00",
            generateRandomDateWithinCurrentMonth(),
            generateRandomFIO(),
            "выездная проверка",
            Random.nextInt(10000,99999).toString(),
            "Подготовить паспорт объекта"
        ),
        Note(
            UUID.randomUUID().toString(),
            "проверка пожарной безопасности 5",
            "8:30-9:00",
            generateRandomDateWithinCurrentMonth(),
            generateRandomFIO(),
            "выездная проверка",
            Random.nextInt(10000,99999).toString(),
            "Подготовить паспорт объекта"
        )
    )
}