package com.example.opencontrol.view.noteTab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.R
import com.example.opencontrol.model.UserRole
import com.example.opencontrol.model.networkDTOs.AppointmentsInLocalDateTime
import com.example.opencontrol.ui.theme.GreenStatus
import com.example.opencontrol.ui.theme.GreyText
import com.example.opencontrol.ui.theme.LightColors
import com.example.opencontrol.ui.theme.LightGrey
import com.example.opencontrol.ui.theme.YellowStatus
import com.example.opencontrol.view.EndEditingBlock
import com.example.opencontrol.view.HeaderBlock
import com.example.opencontrol.view.chatTab.VideoScreen
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

data class NoteInfoScreen(val noteId: String) : Screen {
    @Composable
    override fun Content() {
        NoteInfoContent(noteId = noteId)
    }
}

@Composable
private fun NoteInfoContent(noteId: String) {
    Timber.d("@@@ NoteInfo noteId = $noteId")
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<MainViewModel>()
    val openDialog = remember { mutableStateOf(false) }

    val note = viewModel.getAppointmentById(noteId)
    if (note != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderBlock("Информация о записи")
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item { VideoBlock() }
                    item { NoteInfoBlock(note) }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            ImageBoxDummy()
                            ImageBoxDummy()
                        }
                    }
                }
                NoteStatusBlock(note)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                if (viewModel.userRole == UserRole.BUSINESS){
                    EndEditingBlock(
                        textOnDismiss = "Перенести",
                        onDismiss = {
                            Timber.d("@@@ Перенести")
                            viewModel.cancelConsultation(note.id)
                            navigator.replace(NewNoteScreen())
                        },
                        textOnConfirm = "Отменить",
                        onConfirm = {
                            openDialog.value = true
                            Timber.d("@@@ Отменить")
                        }
                    )
                    if (openDialog.value) {
                        DeleteNoteDialog(onConfirm = {
                            viewModel.cancelConsultation(note.id)
                            openDialog.value = false
                            navigator.pop()
                        },
                            onDismiss = { openDialog.value = false })
                    }
                } else {
                    EndEditingBlock(
                        textOnDismiss = "Отменить",
                        onDismiss = {
                            openDialog.value = true
                            viewModel.cancelConsultation(note.id)
                        },
                        textOnConfirm = "Подтвердить",
                        onConfirm = {
                            Timber.d("@@@ Подтвердить")
                            viewModel.agreeAppointment(noteId)
                        }
                    )
                    if (openDialog.value) {
                        DeleteNoteDialog(onConfirm = {
                            viewModel.cancelConsultation(note.id)
                            openDialog.value = false
                            navigator.pop()
                        },
                            onDismiss = { openDialog.value = false })
                    }
                }

            }
        }
    } else navigator.pop()
}

@Composable
private fun VideoBlock() {
    val navigator = LocalNavigator.currentOrThrow
    Button(
        onClick = { navigator.push(VideoScreen("roomName")) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(32),
        colors = ButtonDefaults.buttonColors(containerColor = LightGrey, contentColor = Color.Black)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.video_icon),
            contentDescription = "",
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = "Начать консультирование",
            modifier = Modifier
                .padding(8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun NoteInfoBlock(note: AppointmentsInLocalDateTime) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        NoteTextBlock("Kонтрольно-надзорный орган", note.knoName)
        NoteTextBlock("Вид контроля", note.measureName)
        NoteTextBlock("Тип встречи", "Видео-Конференция")
        NoteTextBlock("Дополнительная информация", "У меня нет вебкамеры")
    }
}

@Composable
private fun NoteTextBlock(title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 4.dp),
            color = GreyText
        )
        Text(text = value, fontSize = 14.sp, modifier = Modifier.padding(vertical = 4.dp))
    }
}

@Composable
private fun ImageBoxDummy() {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(12))
            .aspectRatio(1f)
            .height(100.dp)
            .background(color = LightGrey),
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier
                .offset(x = (-5).dp, y = 5.dp)
                .height(15.dp)
                .clip(CircleShape)
                .background(LightColors.primary)
                .aspectRatio(1f)
        ) {
            androidx.compose.material.Icon(Icons.Filled.Close, null, tint = LightColors.onPrimary)
        }
    }
}

@Composable
private fun NoteStatusBlock(note: AppointmentsInLocalDateTime) {
    Row(modifier = Modifier.fillMaxWidth()) {
        if (note.status == "SELECTED") {
            Text(
                text = "Ожидает подтверждения",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = LightColors.onPrimary,
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = YellowStatus,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(4.dp)

            )
        } else {
            Text(
                text = "Запись подтверждена",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = LightColors.onPrimary,
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = GreenStatus,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(4.dp)
            )
        }
    }
}

@Composable
private fun DeleteNoteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Отменить запись?") },
        text = { Text("Вы действительно хотите отменить эту запись?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Да")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Нет")
            }
        }
    )
}
