package com.example.opencontrol.view.userProfileTab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

class UserDetailInfoScreen: Screen {
    @Composable
    override fun Content() {
        UserDetailInfoScreenContent()
    }
}

@Composable
private fun UserDetailInfoScreenContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "UserInfoScreenContent")
    }
}