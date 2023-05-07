package com.example.opencontrol

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.opencontrol.model.Note
import com.example.opencontrol.noteTab.MyCalendarView
import com.example.opencontrol.noteTab.NoteInfo
import timber.log.Timber
import java.time.LocalDate
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteTab() {
    var selectedDate by remember { mutableStateOf<LocalDate>(LocalDate.now()) }
    val testingNote = Note(
        UUID.randomUUID().toString(),
        "проверка пожарной безопасности",
        "8:30-9:00",
        LocalDate.now(),
        "Васильев Александр Ильич",
        "выездная проверка",
        "123456789",
        "Подготовить паспорт объекта"
    )
    Column() {
        val screenNavController = rememberNavController()
        NavHost(screenNavController, startDestination = Tab.NoteTab.route) {
            composable(Tab.NoteTab.route) { NoteTab() }
            composable(MainActivity.Screen.NoteInfo.route) { NoteInfo() }
        }
        MyCalendarView(selectedDate) { selectedDate = it }
        MyNotesAndButtonsRow()
        LazyColumn() {
            items(5) {
                NoteCard(
                    testingNote
                )
            }
        }
    }
    Timber.d("@@@ Selected date = $selectedDate")
}

@Composable
private fun MyNotesAndButtonsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Мои записи",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
        Button(onClick = { /*TODO*/ }) {
            Text(
                text = "новая запись",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Button(onClick = { /*TODO*/ }) {
            Text(
                text = "фильтр",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun NoteCard(note: Note) {
    Card(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = note.type,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Инспектор: ${note.inspectorFIO}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraLight
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = note.time,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight
            )
            Text(
                text = "${note.date.dayOfMonth}.${note.date.monthValue}.${note.date.year}",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight
            )
        }
    }
}
