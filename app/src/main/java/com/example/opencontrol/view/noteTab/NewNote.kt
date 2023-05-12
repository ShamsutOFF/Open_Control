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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.ui.theme.LightColors
import com.example.opencontrol.ui.theme.Typography
import com.example.opencontrol.ui.theme.WeeklyCalendarSelectedDateBackground
import com.example.opencontrol.ui.theme.WeeklyCalendarSelectedDateText
import com.example.opencontrol.ui.theme.md_theme_light_primary
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

@Destination
@Composable
fun NewNote(navigator: DestinationsNavigator) {
    val viewModel = getViewModel<MainViewModel>()
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderBlock()
        LazyColumn(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { SelectableItem("Kонтрольно-надзорный орган", viewModel.getControlAgencies()) }
            item { SelectableItem("Вид контроля", viewModel.getControlTypes()) }
            item { SelectableItem("Подразделение", viewModel.getDepartments()) }
            item { ToggleItem("Выберите тип встречи") }
            item { ExtraInfoItem("Дополнительная информация") }
            item { AddPhotoItem() }
            item { WeeklyCalendar(viewModel.selectedDate, { }) }
            item { FreeTimeForRecording(viewModel.getFreeTimeForRecording(5)) }
            item { NoteButton(navigator) }
        }
    }
}

@Composable
private fun FreeTimeForRecording(freeTimeForRecording: List<String>) {
    var selectedTime by remember {
        mutableStateOf("")
    }
    LazyRow() {
        items(freeTimeForRecording) { time ->
            SelectableTimeCell(time, time == selectedTime) { selectedTime = time }
        }
    }
}


@Composable
private fun SelectableTimeCell(
    time: String,
    isSelected: Boolean,
    onTimeSelected: (String) -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val cellWidth = (screenWidthDp - 60.dp) / 3
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
            text = time,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = if (isSelected) LightColors.onPrimary else Color.Unspecified
        )
    }
}


@Composable
private fun HeaderBlock() {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var width by remember { mutableStateOf(Int.MIN_VALUE) }
        val widthText = LocalDensity.current.run { width.toDp() }

        Box(Modifier.onGloballyPositioned { coordinates ->
            width = coordinates.size.width
        }) {
            Text(text = "Новая запись", style = Typography.headlineMedium)
        }
        Divider(
            color = md_theme_light_primary,
            thickness = 3.dp,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .width(widthText)
        )
    }
}

@Composable
private fun SelectableItem(title: String, values: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("нажмите для выбора") }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = title, fontSize = 14.sp)
        OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = { expanded = true }) {
            Text(text = selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            values.forEachIndexed { index, it ->
                DropdownMenuItem(text = { Text(text = it) }, onClick = {
                    selectedOption = it
                    expanded = false
                })
                if (index != values.size - 1) Divider()
            }
        }
    }
}

@Composable
private fun ToggleItem(title: String) {
    var checkedfirst by remember { mutableStateOf(false) }
    var checkedSecond by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = title, fontSize = 14.sp)
        Row(modifier = Modifier.fillMaxWidth()) {
            FilledIconToggleButton(
//            FilledTonalIconToggleButton(
                checked = checkedfirst, onCheckedChange = {
                    if (!checkedfirst) {
                        checkedfirst = true
                        checkedSecond = false
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = "ВИДЕО-КОНФЕРЕНЦИЯ", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            FilledIconToggleButton(
                checked = checkedSecond, onCheckedChange = {
                    if (!checkedSecond) {
                        checkedSecond = true
                        checkedfirst = false
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = "ЛИЧНЫЙ ВИЗИТ", fontSize = 14.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun ExtraInfoItem(title: String) {
    var extraText by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = title, fontSize = 14.sp)
        OutlinedTextField(
            value = extraText,
            label = { Text(text = "Комментарий к проверке") },
            onValueChange = { extraText = it },
            modifier = Modifier
//                .padding(vertical = 8.dp)
                .fillMaxWidth(),
//                .clearFocusOnKeyboardDismiss(),
            shape = androidx.compose.foundation.shape.CircleShape,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            })
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPhotoItem() {
    var selectedImagesUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val multiplePhotoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(5),
            onResult = { uris -> selectedImagesUris = uris })

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
//        modifier = Modifier.height(100.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        items(selectedImagesUris) { uri ->
            OutlinedCard(
                onClick = { selectedImagesUris = selectedImagesUris.filter { it != uri } },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier.height(100.dp),
//                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
private fun NoteButton(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            Timber.d("@@@ Implement it!!!")
        }) {
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