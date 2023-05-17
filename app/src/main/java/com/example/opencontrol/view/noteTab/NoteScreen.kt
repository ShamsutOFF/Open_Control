package com.example.opencontrol.view.noteTab

import androidx.compose.foundation.background
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.model.Note
import com.example.opencontrol.ui.theme.GreyBackground
import com.example.opencontrol.ui.theme.LightColors
import org.koin.androidx.compose.getViewModel
import timber.log.Timber
import java.time.format.DateTimeFormatter
import java.util.Locale


class NoteScreen : Screen {
    @Composable
    override fun Content() {
        Timber.d("@@@ Content()")
        NoteTabContent()
    }
}

@Composable
private fun NoteTabContent() {
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
    Column(modifier = Modifier.background(GreyBackground)) {
        MonthlyCalendar(
            selectedDate = viewModel.selectedDate,
            markedDateList = markedDateList,
            onDateSelected = viewModel::changeSelectedDate
        )
        MyNotesAndButtonsRow()
        LazyColumn(state = listState) {
            items(notes) { note ->
                NoteCard(note = note)
            }
        }
    }
}

@Composable
private fun MyNotesAndButtonsRow() {
    val navigator = LocalNavigator.currentOrThrow
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
        Button(onClick = { navigator.push(NewNoteScreen()) }) {
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
private fun NoteCard(note: Note) {
    val navigator = LocalNavigator.currentOrThrow
    Card(
        onClick = { navigator.push(NoteInfoScreen(note.id)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightColors.onPrimary,
        )

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
