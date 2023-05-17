package com.example.opencontrol.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.opencontrol.ui.theme.SelectedTab
import com.example.opencontrol.ui.theme.UnSelectedTab
import com.example.opencontrol.view.chatTab.ChatTab
import com.example.opencontrol.view.homeTab.HomeTab
import com.example.opencontrol.view.noteTab.NoteTab
import com.example.opencontrol.view.userProfileTab.UserProfileTab

object MainScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        TabNavigator(HomeTab) {
            Scaffold(
                content = {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(it)){
                        CurrentTab()
                    }
                },
                bottomBar = {
                    BottomNavigation(backgroundColor = Color.White) {
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(NoteTab)
                        TabNavigationItem(ChatTab)
                        TabNavigationItem(UserProfileTab)
                    }
                }
            )
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current.key == tab.key,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                painter = tab.options.icon!!,
                contentDescription = tab.options.title
            )
        },
        selectedContentColor = SelectedTab, unselectedContentColor = UnSelectedTab
    )
}