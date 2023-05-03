package com.example.opencontrol

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.opencontrol.ui.theme.OpenControlTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenControlTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = Screen.Login.route) {
                        composable(Screen.Login.route) { LoginScreen(navController) }
                        composable(Screen.Main.route) { MainScreen() }
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomNavigation(backgroundColor = Color.White) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val tabs = listOf(
                        Screen.Tab1,
                        Screen.Tab2,
                        Screen.Tab3,
                        Screen.Tab4
                    )
                    tabs.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    screen.icon,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    navController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = Screen.Tab1.route,
                Modifier.padding(innerPadding)
            ) {
                composable(Screen.Tab1.route) { FirstScreen() }
                composable(Screen.Tab2.route) { SecondScreen() }
                composable(Screen.Tab3.route) { ThirdScreen() }
                composable(Screen.Tab4.route) { FourthScreen() }
            }
        }
    }

    sealed class Screen(val route: String, val icon: ImageVector) {
        object Login : Screen("login", Icons.Outlined.Home)
        object Main : Screen("main", Icons.Outlined.Home)
        object Tab1 : Screen("tab1", Icons.Outlined.Home)
        object Tab2 : Screen("tab2", Icons.Outlined.Edit)
        object Tab3 : Screen("tab3", Icons.Outlined.MailOutline)
        object Tab4 : Screen("tab4", Icons.Outlined.Person)
    }
}