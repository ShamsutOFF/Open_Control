package com.example.opencontrol

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import timber.log.Timber
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SecondScreen() {
    val selectedDate = remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        MyCalendarView(LocalDate.now()) { Timber.d("Selected date = $it") }
    }
}
