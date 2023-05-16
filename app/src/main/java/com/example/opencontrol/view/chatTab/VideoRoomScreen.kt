package com.example.opencontrol.view.chatTab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.collectLatest

class VideoRoomScreen : Screen {
    @Composable
    override fun Content() {
        RoomScreenContent()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoomScreenContent(
    viewModel: VideoRoomScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val navigator = LocalNavigator.currentOrThrow
    LaunchedEffect(key1 = true) {
        viewModel.onJoinEvent.collectLatest { name ->
            navigator.push(VideoScreen(roomName = name))
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        TextField(
            value = viewModel.roomName.value.text,
            onValueChange = viewModel::onRoomEnter,
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.roomName.value.error != null,
            placeholder = { Text(text = "Enter a room name") })

        viewModel.roomName.value.error?.let {
            Text(text = it, color = MaterialTheme.colors.error)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = viewModel::onJoinRoom) {
            Text(text = "Join")
        }
    }
}