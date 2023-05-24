package com.example.opencontrol.view.chatTab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

class ChatScreen : Screen {
    @Composable
    override fun Content() {
        ChatScreenContent()
    }
}

@Composable
private fun ChatScreenContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "ChatScreenContent")
    }
}