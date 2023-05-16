package com.example.opencontrol.view.chatTab

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

data class ChatMessagesScreen(val channelId: String) : Screen {
    @Composable
    override fun Content() {
        ChatMessagesScreenContent(channelId)
    }

    @Composable
    private fun ChatMessagesScreenContent(channelId: String) {
        // 1 - Load the ID of the selected channel
        val navigator = LocalNavigator.currentOrThrow
        if (channelId.isBlank()) {
            navigator.pop()
            return
        }

        // 2 - Add the MessagesScreen to your UI
        ChatTheme {
            MessagesScreen(
                channelId = channelId,
                messageLimit = 30,
                onBackPressed = {
                    navigator.pop()
                }
            )
        }
    }
}
