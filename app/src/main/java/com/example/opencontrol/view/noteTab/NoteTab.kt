package com.example.opencontrol.view.noteTab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.model.Note
import com.example.opencontrol.view.destinations.NewNoteDestination
import com.example.opencontrol.view.destinations.NoteInfoDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

@Destination
@Composable
fun NoteTab(navigator: DestinationsNavigator) {
    val viewModel = getViewModel<MainViewModel>()
    val markedDateList = viewModel.getAllNotes().map { note ->
        note.date
    }
    val listState = rememberLazyListState()

    val notes = viewModel.getAllNotes().sortedBy { it.date }
    val selectedIndex = notes.indexOfFirst { it.date >= viewModel.selectedDate }
    val scrollPosition = if (selectedIndex != -1) selectedIndex else notes.lastIndex

    LaunchedEffect(key1 = viewModel.selectedDate) {
        listState.animateScrollToItem(index = scrollPosition)
    }
    Column() {
        MyCalendarView(
            selectedDate = viewModel.selectedDate,
            markedDateList = markedDateList,
            onDateSelected = viewModel::changeSelectedDate
        )
        MyNotesAndButtonsRow(navigator)
        LazyColumn(state = listState) {
            items(notes) { note ->
                NoteCard(note = note, navigator = navigator)
            }
        }
    }
}

@Composable
private fun MyNotesAndButtonsRow(navigator: DestinationsNavigator) {
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
        Button(onClick = { navigator.navigate(NewNoteDestination) }) {
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
private fun NoteCard(note: Note, navigator: DestinationsNavigator) {
    Card(
        onClick = { navigator.navigate(NoteInfoDestination(note.id)) },
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
            val formatter1 = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))
            Text(
                text = note.time,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight
            )
            Text(
                text = note.date.format(formatter1),
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight
            )
        }
    }
}
