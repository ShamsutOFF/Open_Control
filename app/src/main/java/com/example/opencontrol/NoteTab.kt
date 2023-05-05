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
import timber.log.Timber
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteTab() {
    var selectedDate by remember { mutableStateOf<LocalDate>(LocalDate.now()) }
    Column() {
        MyCalendarView(LocalDate.now()) { selectedDate = it }
        MyNotesAndButtonsRow()
        LazyColumn() {
            items(5) { NoteCard() }
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun NoteCard() {
    Card(
        onClick = { Timber.d("@@@ Click to Card!") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "проверка пожарной безопасности",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Инспектор: Васильев Александ Ильич",
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
        ) {
            Text(
                text = "8:30-9:00",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight
            )
            Text(
                text = "14 августа 2023",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight
            )
        }
    }
}
