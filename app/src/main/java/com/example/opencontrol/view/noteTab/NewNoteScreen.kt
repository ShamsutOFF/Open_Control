package com.example.opencontrol.view.noteTab

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.model.networkDTOs.FreeWindowInLocalDateTime
import com.example.opencontrol.ui.theme.LightColors
import com.example.opencontrol.view.EnterTextInfoItemBlock
import com.example.opencontrol.view.HeaderBlock
import com.example.opencontrol.view.SelectableItemBlock
import org.koin.androidx.compose.getViewModel
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NewNoteScreen : Screen {
    @Composable
    override fun Content() {
        NewNoteContent()
    }
}

@Composable
private fun NewNoteContent() {
    val viewModel = getViewModel<MainViewModel>()
    val navigator = LocalNavigator.currentOrThrow
    val knosNames = viewModel.listOfAllKnos.map {
        it.name
    }
    val measuresForKno = viewModel.measuresForKno.map {
        it.name
    }
    var selectedWindow by remember {
        mutableStateOf(FreeWindowInLocalDateTime("", LocalDateTime.now()))
    }
    Timber.d("@@@ selectedWindow = $selectedWindow")
    var extraText by remember {
        mutableStateOf("нажмите для выбора")
    }
    var selectedKno by remember {
        mutableStateOf("нажмите для выбора")
    }
    var selectedMeasure by remember {
        mutableStateOf("нажмите для выбора")
    }
    LaunchedEffect(key1 = selectedKno) {
        val kno = viewModel.getKnoByName(selectedKno)
        if (kno != null) {
            viewModel.getMeasuresForKno(kno.id.toString())
            viewModel.getFreeWindows(kno.id.toString())
        }
        selectedMeasure = "нажмите для выбора"
    }
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderBlock("Новая запись")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                SelectableItemBlock("Kонтрольно-надзорный орган", knosNames, selectedKno) {
                    Timber.d("@@@ selected = $it")
                    selectedKno = it
                }
            }
            item {
                SelectableItemBlock("Вид контроля", measuresForKno, selectedMeasure) {
                    selectedMeasure = it
                }
            }
            item {
                EnterTextInfoItemBlock(
                    "Дополнительная информация",
                    "Введите комментарий к проверке",
                    extraText
                ) { extraText = it }
            }
            item { AddPhotoItem() }
            item { AddDocItem() }
            item { WeeklyCalendar(viewModel.selectedDate, viewModel::changeSelectedDate) }
            item {
                FreeTimeForRecording(
                    viewModel.getFreeTimeForSelectedDate(),
                    selectedWindow
                ) { selectedWindow = it }
            }
            item {
                NoteButton(
                    selectedMeasure != "нажмите для выбора" && selectedWindow.id.isNotBlank()
                ) {
                    Timber.d("@@@ Будем записываться")
                    viewModel.noteMeToConsultation(selectedWindow.id, selectedMeasure)
                    navigator.pop()
                }
            }
        }
    }
}

@Composable
private fun AddPhotoItem() {
    val viewModel = getViewModel<MainViewModel>()
    val multiplePhotoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(5),
            onResult = { uris -> viewModel.photoUris.addAll(uris) })
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        ClickableText(
            text = AnnotatedString("+ Добавить фото"),
            onClick = {
                multiplePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            })
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        items(viewModel.photoUris) { uri ->
            ImageBox(uri = uri) { delUri ->
                viewModel.photoUris.remove(delUri)
            }
        }
    }
}

@Composable
private fun AddDocItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        ClickableText(
            text = AnnotatedString("+ Добавить документ"),
            onClick = {
                Timber.d("@@@ Click to AddDoc")
            })
    }
}

@Composable
private fun ImageBox(uri: Uri, deletePhoto: (Uri) -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(12))
            .aspectRatio(1f),
        contentAlignment = Alignment.TopEnd
    ) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier.height(100.dp),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .offset(x = (-5).dp, y = 5.dp)
                .height(15.dp)
                .clip(CircleShape)
                .background(LightColors.primary)
                .aspectRatio(1f)
                .clickable { deletePhoto(uri) }
        ) {
            Icon(Icons.Filled.Close, null, tint = LightColors.onPrimary)
        }
    }
}

@Composable
private fun FreeTimeForRecording(
    freeTimeForRecording: List<FreeWindowInLocalDateTime>,
    selectedTime: FreeWindowInLocalDateTime,
    onTimeSelected: (FreeWindowInLocalDateTime) -> Unit
) {
    LazyRow() {
        items(freeTimeForRecording) { window ->
            SelectableTimeChip(
                window,
                window == selectedTime
            ) { onTimeSelected(it) }
        }
    }
}

@Composable
private fun SelectableTimeChip(
    time: FreeWindowInLocalDateTime,
    isSelected: Boolean,
    onTimeSelected: (FreeWindowInLocalDateTime) -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val cellWidth = (screenWidthDp - 60.dp) / 3
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val formattedTime = time.appointmentTime.format(formatter)
    val formattedPlusHour = time.appointmentTime.plusHours(1).format(formatter)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(50))
            .width(cellWidth)
            .background(
                when {
                    isSelected -> LightColors.primary
                    else -> LightColors.primaryContainer
                }
            )
            .clickable { onTimeSelected(time) }
            .aspectRatio(3f)
    ) {
        Text(
            text = "$formattedTime - $formattedPlusHour",
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = if (isSelected) LightColors.onPrimary else Color.Unspecified
        )
    }
}

@Composable
private fun NoteButton(enabled: Boolean, function: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            enabled = enabled,
            onClick = {
                function()
            }, shape = RoundedCornerShape(32)
        ) {
            Text(
                text = "Записаться",
                modifier = Modifier
                    .padding(8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}