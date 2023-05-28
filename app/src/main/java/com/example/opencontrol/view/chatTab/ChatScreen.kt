package com.example.opencontrol.view.chatTab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.model.ChatMessage
import com.example.opencontrol.ui.theme.ChatGreenPrimary
import com.example.opencontrol.ui.theme.ChatGreenSecondary
import com.example.opencontrol.ui.theme.ChatGreyMessage
import com.example.opencontrol.ui.theme.LightColors
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
    val messages = viewModel.chatListOfMessages

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .weight(8f)
        ) {
            MessageList(messages = messages)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
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
fun MessageList(messages: List<ChatMessage>) {
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = messages.size) {
        listState.animateScrollToItem(index = messages.size)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        state = listState
    ) {
        items(messages) { message ->
            MessageItem(message = message)
        }
    }
}

@Composable
fun MessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        if (message.isSentByUser) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = message.content,
                color = LightColors.onPrimary,
                modifier = Modifier
                    .background(
                        color = ChatGreenPrimary,
                        shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 8.dp)
                    )
                    .padding(8.dp)
            )
        } else {
            Text(
                text = message.content,
                modifier = Modifier
                    .background(
                        color = ChatGreyMessage,
                        shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 0.dp)
                    )
                    .padding(8.dp)
            )
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
            .background(ChatGreenSecondary)
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