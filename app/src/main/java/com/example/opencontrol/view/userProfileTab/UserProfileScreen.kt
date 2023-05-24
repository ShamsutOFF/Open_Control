package com.example.opencontrol.view.userProfileTab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

class UserProfileScreen: Screen {
    @Composable
    override fun Content() {
        UserProfileScreenContent()
    }
}

@Composable
private fun UserProfileScreenContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "UserProfileScreenContent")
    }
}