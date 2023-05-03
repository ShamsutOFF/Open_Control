package com.example.opencontrol

import android.widget.CalendarView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import java.util.Calendar

@Composable
fun SecondScreen() {
    val selectedDate = remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        val calendar = Calendar.getInstance()
        AndroidView(modifier = Modifier.fillMaxWidth(), factory = { CalendarView(it) }, update = {
            it.firstDayOfWeek = Calendar.MONDAY
            it.setOnDateChangeListener { calendarView, year, mounth, day ->
                selectedDate.value = "$day.${mounth+1}.$year"
            }
        })
        Text(text = selectedDate.value)
    }
}