package com.example.opencontrol.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.R
import com.example.opencontrol.ui.theme.SelectedTab
import com.example.opencontrol.ui.theme.UnSelectedTab
import com.example.opencontrol.view.noteTab.NoteTab
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

//private val mainViewModel: MainViewModel by viewModel()
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(screenNavController: NavHostController) {
    val tabNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(backgroundColor = Color.White) {
                val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val tabs = listOf(
                    Tab.HomeTab,
                    Tab.NoteTab,
                    Tab.ChatTab,
                    Tab.UserTab
                )
                tabs.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                ImageVector.vectorResource(screen.icon),
                                contentDescription = null,
                            )
                        },
                        selectedContentColor = SelectedTab,
                        unselectedContentColor = UnSelectedTab,
                        selected = currentRoute == screen.route,
                        onClick = {
                            tabNavController.navigate(screen.route) {
                                tabNavController.graph.startDestinationRoute?.let { route ->
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
            tabNavController,
            startDestination = Tab.HomeTab.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Tab.HomeTab.route) { HomeTab() }
            composable(Tab.NoteTab.route) { NoteTab(screenNavController) }
            composable(Tab.ChatTab.route) { ChatTab() }
            composable(Tab.UserTab.route) { UserTab() }
        }
    }
}

sealed class Tab(val route: String, val icon: Int) {
    object HomeTab : Tab("tab1", R.drawable.home_icon)
    object NoteTab : Tab("tab2", R.drawable.note_icon)
    object ChatTab : Tab("tab3", R.drawable.chat_icon)
    object UserTab : Tab("tab4", R.drawable.user_icon)
}