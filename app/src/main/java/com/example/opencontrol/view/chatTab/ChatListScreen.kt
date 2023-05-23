package com.example.opencontrol.view.chatTab

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class ChatListScreen : Screen {
    @Composable
    override fun Content() {
        ChatListScreenContent()
    }
}

@Composable
private fun ChatListScreenContent() {
    Text("Здесь будет бот")
}