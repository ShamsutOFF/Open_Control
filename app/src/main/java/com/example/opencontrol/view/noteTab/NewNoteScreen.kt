package com.example.opencontrol.view.noteTab

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import coil.compose.AsyncImage
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.ui.theme.GreyText
import com.example.opencontrol.ui.theme.LightColors
import com.example.opencontrol.ui.theme.LightGreyBorder
import com.example.opencontrol.view.EnterInfoItemBlock
import com.example.opencontrol.view.HeaderBlock
import com.example.opencontrol.view.SelectableItemBlock
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

class NewNoteScreen : Screen {
    @Composable
    override fun Content() {
        NewNoteContent()
    }
}

@Composable
private fun NewNoteContent() {
    val viewModel = getViewModel<MainViewModel>()
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
            item { SelectableItemBlock("Kонтрольно-надзорный орган", viewModel.getControlAgencies()) }
            item { SelectableItemBlock("Вид контроля", viewModel.getControlTypes()) }
            item { SelectableItemBlock("Подразделение", viewModel.getDepartments()) }
            item { ToggleItem("Выберите тип встречи") }
            item { EnterInfoItemBlock("Дополнительная информация", "Введите комментарий к проверке") }
            item { AddPhotoItem() }
            item { WeeklyCalendar(viewModel.selectedDate, { }) }
            item { FreeTimeForRecording(viewModel.getFreeTimeForRecording(5).distinct()) }
            item { NoteButton() }
        }
    }
}
//
//@Composable
//private fun SelectableItem(title: String, values: List<String>) {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedOption by remember { mutableStateOf("нажмите для выбора") }
//    Column(
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth()
//    ) {
//        Text(text = title, fontSize = 14.sp)
//        OutlinedButton(
//            modifier = Modifier
//                .fillMaxWidth(),
//            shape = RoundedCornerShape(32),
//            onClick = { expanded = true },
//            colors = ButtonDefaults.outlinedButtonColors(contentColor = GreyText)
//        ) {
//            Text(text = selectedOption)
//        }
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            values.forEachIndexed { index, it ->
//                DropdownMenuItem(text = { Text(text = it) }, onClick = {
//                    selectedOption = it
//                    expanded = false
//                })
//                if (index != values.size - 1) Divider()
//            }
//        }
//    }
//}

@Composable
private fun ToggleItem(title: String) {
    var selectedItem by remember {
        mutableStateOf("")
    }
    val firstButtonText = "ВИДЕО-КОНФЕРЕНЦИЯ"
    val secondButtonText = "ЛИЧНЫЙ ВИЗИТ"
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = title, fontSize = 14.sp)
        Row(modifier = Modifier.fillMaxWidth()) {
            ToggleButton(firstButtonText, selectedItem == firstButtonText) { selectedItem = it }
            ToggleButton(secondButtonText, selectedItem == secondButtonText) { selectedItem = it }
        }
    }
}


@Composable
private fun ToggleButton(
    text: String,
    isSelected: Boolean,
    onTextSelected: (String) -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val cellWidth = (screenWidthDp - 16.dp) / 2
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(32))
            .width(cellWidth)
            .height(40.dp)
            .background(
                when {
                    isSelected -> LightColors.primary
                    else -> Color.Transparent
                }
            )
            .border(
                width = when {
                    isSelected -> 0.dp
                    else -> 1.dp
                }, color = when {
                    isSelected -> Color.Transparent
                    else -> LightGreyBorder
                }, shape = RoundedCornerShape(32)
            )
            .clickable { onTextSelected(text) }
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (isSelected) LightColors.onPrimary else GreyText
        )
    }
}

//@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
//@Composable
//private fun ExtraInfoItem(title: String) {
//    var extraText by remember {
//        mutableStateOf("")
//    }
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val focusManager = LocalFocusManager.current
//    Column(
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth()
//    ) {
//        Text(text = title, fontSize = 14.sp)
//        OutlinedTextField(
//            value = extraText,
//            label = { Text(text = "Комментарий к проверке") },
//            onValueChange = { extraText = it },
//            modifier = Modifier
//                .fillMaxWidth(),
//            shape = RoundedCornerShape(32),
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//            keyboardActions = KeyboardActions(onDone = {
//                keyboardController?.hide()
//                focusManager.clearFocus()
//            })
//        )
//    }
//}

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
fun ImageBox(uri: Uri, deletePhoto: (Uri) -> Unit) {
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
private fun FreeTimeForRecording(freeTimeForRecording: List<String>) {
    var selectedTime by remember {
        mutableStateOf("")
    }
    LazyRow() {
        items(freeTimeForRecording) { time ->
            SelectableTimeChip(time, time == selectedTime) { selectedTime = time }
        }
    }
}

@Composable
private fun SelectableTimeChip(
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
private fun NoteButton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                Timber.d("@@@ Implement it!!!")
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