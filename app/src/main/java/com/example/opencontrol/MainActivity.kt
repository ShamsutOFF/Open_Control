package com.example.opencontrol

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.opencontrol.ui.theme.OpenControlTheme
import com.example.opencontrol.view.MainScreen
import com.example.opencontrol.view.enterScreen.EnterScreen
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenControlTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val modelView = getViewModel<MainViewModel>()
//                    val startScreen = if (modelView.loggedIn()) {
//                        MainScreen
//                    } else {
//                        EnterScreen()
//                    }
//                    Navigator(screen = startScreen) { navigator ->
                    Navigator(screen = EnterScreen()) { navigator ->
                        SlideTransition(navigator = navigator) {
                            it.Content()
                        }
                    }
                }
            }
        }
    }
}