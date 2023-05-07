package com.example.opencontrol.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.opencontrol.MainActivity
import com.example.opencontrol.MainViewModel
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

@Composable
fun LoginScreen(navController: NavController) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "LoginScreen")
            Button(onClick = { navController.navigate(MainActivity.Screen.Main.route) }) {
                Text(text = "Войти")
            }
        }

    }
}