package com.example.opencontrol.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.opencontrol.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

@Destination
@Composable
fun HomeTab(navigator: DestinationsNavigator) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val viewModel = getViewModel<MainViewModel>()
        Timber.d("@@@ HomeTab viewModelId = ${viewModel.viewModelId}")
    }
}