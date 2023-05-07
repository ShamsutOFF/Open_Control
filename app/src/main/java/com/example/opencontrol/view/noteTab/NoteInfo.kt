package com.example.opencontrol.view.noteTab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.R
import com.example.opencontrol.model.Note
import com.example.opencontrol.ui.theme.GreyDivider
import com.example.opencontrol.ui.theme.Typography
import com.example.opencontrol.ui.theme.md_theme_light_inversePrimary
import com.example.opencontrol.ui.theme.md_theme_light_primary
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteInfo(noteId: String) {
    Timber.d("@@@ NoteInfo noteId = $noteId")
    val viewModel = getViewModel<MainViewModel>()
    val note = viewModel.getNoteById(noteId)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderBlock()
        ChatAndVideoBlock()
        NoteInfoBlock(note)
        CancelButton()
    }
}

@Composable
private fun HeaderBlock() {
    Text(text = "Информация о записи", style = Typography.headlineLarge)
    Divider(
        color = md_theme_light_primary,
        thickness = 3.dp,
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun ChatAndVideoBlock() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .background(color = md_theme_light_inversePrimary, shape = RoundedCornerShape(16.dp))

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.chat_icon),
                contentDescription = "",
                modifier = Modifier
                    .rotate(90f)
                    .size(30.dp)
            )
            Text(
                text = "Чат с инспектором",
                modifier = Modifier
                    .padding(8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.video_icon),
                contentDescription = "",
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = "Видео-конференция",
                modifier = Modifier
                    .padding(8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun NoteInfoBlock(note: Note) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        FieldInfoRow("Тип:", note.type)
        InfoBlockDivider()
        FieldInfoRow("Дата:", note.date.toString())
        InfoBlockDivider()
        FieldInfoRow("Время:", note.time)
        InfoBlockDivider()
        FieldInfoRow("Формат:", note.format)
        InfoBlockDivider()
        FieldInfoRow("Номер объекта:", note.objectNumber)
        InfoBlockDivider()
        FieldInfoRow("Инспектор:", note.inspectorFIO)
        InfoBlockDivider()
        FieldInfoRow("Дополнительно:", note.info)
    }
}

@Composable
private fun FieldInfoRow(parameter:String, value:String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = parameter,
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Light
        )
        Text(
            text = value,
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
private fun InfoBlockDivider() {
    Divider(
        color = GreyDivider,
        thickness = 1.dp,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun CancelButton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            Timber.d("@@@ Make cancel call to API (Maybe after Alert Dialog)") }) {
            Text(
                text = "Отменить запись",
                modifier = Modifier
                    .padding(8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}
