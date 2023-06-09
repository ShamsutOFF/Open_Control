package com.example.opencontrol.view.chatTab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.DismissDirection
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.input.rotary.onPreRotaryScrollEvent
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.model.ChatMessage
import com.example.opencontrol.ui.theme.ChatGreenPrimary
import com.example.opencontrol.ui.theme.ChatGreenSecondary
import com.example.opencontrol.ui.theme.ChatGreyMessage
import com.example.opencontrol.ui.theme.LightColors
import com.example.opencontrol.ui.theme.md_theme_light_onPrimary
import com.example.opencontrol.view.noteTab.NewNoteScreen
import com.example.opencontrol.view.noteTab.NoteInfoScreen
import com.example.opencontrol.view.noteTab.NoteScreen
import okhttp3.internal.notify
import org.koin.androidx.compose.getViewModel


class ChatScreen : Screen {
    @Composable
    override fun Content() {
        ChatScreenContent()
    }
}

@Composable
private fun ChatScreenContent() {
    ChatWindow()
}

@Composable
fun ChatWindow() {
    val viewModel = getViewModel<MainViewModel>()
    val messages = remember {
        viewModel.chatListOfMessages
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .weight(4f)
        ) {
            MessageList(messages = messages, modifier = Modifier.fillMaxSize())
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .wrapContentHeight()
        ) {
            HelpButtons()
            ConsultationCompleteButton()
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
        ) {
            SendMessageBar(
                onSendMessage = { message ->
                    messages.add(message)
                }
            )
        }

    }
}

@Composable
fun MessageList(messages: List<ChatMessage>, modifier: Modifier) {
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = messages.size) {
        listState.animateScrollToItem(index = messages.size)
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        state = listState
    ) {
        items(messages) { message ->
            MessageItem(message = message)
        }
    }
}

@Composable
fun HelpButtons() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<MainViewModel>()
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HelpButton(
            text = "Записаться на консультацию",
            onTap = { navigator.push(NewNoteScreen()) }
        )
        HelpButton(
            text = "Ближайшая запись на консультацию",
            onTap = {
                viewModel.getNearestAppointment()
                    ?.let {
                        navigator.push(NoteInfoScreen(noteId = it.id))
                    }
            }
        )
        HelpButton(
            text = "Оценить вероятность нарушений",
            onTap = {}
        )
    }
}

@Composable
fun HelpButton(
    text: String,
    onTap: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12))
            .background(ChatGreenPrimary)
            .clickable { onTap() }
            .width(111.dp)
            .height(80.dp)
        //.fillMaxWidth(0.33f)
        //.fillMaxHeight(0.2f)
        ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = md_theme_light_onPrimary,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
    }
}

@Composable
fun ConsultationCompleteButton() {
    val viewModel = getViewModel<MainViewModel>()
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            //TODO: fix size
            .clip(RoundedCornerShape(20))
            .background(ChatGreenPrimary)
            .clickable {
                viewModel.completeBotConsultation()
            }
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = "Завершить консультацию",
            color = md_theme_light_onPrimary,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 56.dp, vertical = 14.dp),
        )
    }
}

@Composable
fun MessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        if (message.isSentByUser) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = message.content,
                color = LightColors.onPrimary,
                modifier = Modifier
                    .background(
                        color = ChatGreenPrimary,
                        shape = RoundedCornerShape(27.dp, 27.dp, 2.dp, 27.dp)
                    )
                    .fillMaxWidth(0.8f)
                    .padding(horizontal = 22.dp, vertical = 16.dp)
            )
        } else {
            Box {
                Text(
                    text = message.content,
                    modifier = Modifier
                        .background(
                            color = ChatGreyMessage,
                            shape = RoundedCornerShape(27.dp, 27.dp, 27.dp, 2.dp)
                        )
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 22.dp, vertical = 16.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessageBar(onSendMessage: (ChatMessage) -> Unit) {
    var messageText by remember { mutableStateOf("") }
    val viewModel = getViewModel<MainViewModel>()

    Row(
        modifier = Modifier
            .fillMaxSize()
            //.background(ChatGreenSecondary)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            shape = RoundedCornerShape(50.dp),
            onValueChange = { messageText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = ChatGreenPrimary,
                unfocusedIndicatorColor = ChatGreenSecondary,
                selectionColors = TextSelectionColors(ChatGreenPrimary, ChatGreenPrimary),
                cursorColor = ChatGreenPrimary
            ),
            placeholder = { Text(text = "Введите сообщение") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onSendMessage(ChatMessage(messageText, true))
                        viewModel.getAnswerFromChat(messageText)
                        messageText = ""
                    },
                    enabled = messageText.isNotBlank()
                ) {
                    Icon(
                        Icons.Filled.Send,
                        null,
                        tint =
                        if (messageText.isNotBlank()) ChatGreenPrimary
                        else ChatGreenPrimary.copy(alpha = 0.6f)
                    )
                }
            }
        )
    }
}