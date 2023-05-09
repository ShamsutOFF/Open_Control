package com.example.opencontrol.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.opencontrol.MyNavGraphs.mainGraph
import com.example.opencontrol.R
import com.example.opencontrol.ui.theme.SelectedTab
import com.example.opencontrol.ui.theme.UnSelectedTab
import com.example.opencontrol.view.destinations.ChatTabDestination
import com.example.opencontrol.view.destinations.HomeTabDestination
import com.example.opencontrol.view.destinations.NoteTabDestination
import com.example.opencontrol.view.destinations.UserTabDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import timber.log.Timber

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navigator: DestinationsNavigator) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            DestinationsNavHost(
                navController = navController, navGraph = mainGraph
            )
        }
    }
}

@Composable
fun BottomBar(
    navController: NavController
) {
    val currentDestination =
        navController.appCurrentDestinationAsState().value
    Timber.d("@@@ currentDestination = $currentDestination")

    BottomNavigation(backgroundColor = Color.White) {
        BottomBarDestination.values().forEach { destination ->
            BottomNavigationItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    navController.navigate(destination.direction.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(ImageVector.vectorResource(destination.icon), null) },
                selectedContentColor = SelectedTab,
                unselectedContentColor = UnSelectedTab,
            )
        }
    }
}

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: Int
) {
    Home(HomeTabDestination, R.drawable.home_icon),
    Note(NoteTabDestination, R.drawable.note_icon),
    Chat(ChatTabDestination, R.drawable.chat_icon),
    User(UserTabDestination, R.drawable.user_icon),
}