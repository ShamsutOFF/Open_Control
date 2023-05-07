package com.example.opencontrol

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.opencontrol.noteTab.NoteInfo
import com.example.opencontrol.ui.theme.OpenControlTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenControlTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val screenNavController = rememberNavController()
                    NavHost(screenNavController, startDestination = Screen.Login.route) {
                        composable(route = Screen.Login.route) {
                            LoginScreen(screenNavController)
                        }
                        composable(route = Screen.Main.route) {
                            MainScreen(screenNavController)
                        }
                        composable(route = Screen.NoteInfo.route + "/{noteId}",
                            arguments = listOf(
                                navArgument("noteId") {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            backStackEntry.arguments?.getString("noteId")?.let { NoteInfo(it) }
                        }
                    }
                }
            }
        }
    }

    sealed class Screen(val route: String) {
        object Login : Screen("login")
        object Main : Screen("main")
        object NoteInfo : Screen("noteInfo")

        fun withArgs(vararg args: String): String {
            return buildString {
                append(route)
                args.forEach { arg ->
                    append("/$arg")
                }
            }
        }
    }
}