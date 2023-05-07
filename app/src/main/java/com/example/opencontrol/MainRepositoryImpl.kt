package com.example.opencontrol

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.opencontrol.model.Note
import java.time.LocalDate
import java.util.UUID

class MainRepositoryImpl( private val api: MyApi) :MainRepository{

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
            LocalDate.now(),
            "Васильев Александр Ильич",
            "выездная проверка",
            "123456789",
            "Подготовить паспорт объекта"
        ),
        Note(
            UUID.randomUUID().toString(),
            "проверка пожарной безопасности 2",
            "8:30-9:00",
            LocalDate.now(),
            "Васильев Александр Ильич",
            "выездная проверка",
            "123456789",
            "Подготовить паспорт объекта"
        ),
        Note(
            UUID.randomUUID().toString(),
            "проверка пожарной безопасности 3",
            "8:30-9:00",
            LocalDate.now(),
            "Васильев Александр Ильич",
            "выездная проверка",
            "123456789",
            "Подготовить паспорт объекта"
        ),
        Note(
            UUID.randomUUID().toString(),
            "проверка пожарной безопасности 4",
            "8:30-9:00",
            LocalDate.now(),
            "Васильев Александр Ильич",
            "выездная проверка",
            "123456789",
            "Подготовить паспорт объекта"
        ),
        Note(
            UUID.randomUUID().toString(),
            "проверка пожарной безопасности 5",
            "8:30-9:00",
            LocalDate.now(),
            "Васильев Александр Ильич",
            "выездная проверка",
            "123456789",
            "Подготовить паспорт объекта"
        )
    )
}