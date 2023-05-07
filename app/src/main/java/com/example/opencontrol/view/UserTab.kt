package com.example.opencontrol.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.opencontrol.MainViewModel
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

@Composable
fun UserTab() {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {
        val viewModel = getViewModel<MainViewModel>()

        Text(text = "FourthScreen")
    }
}