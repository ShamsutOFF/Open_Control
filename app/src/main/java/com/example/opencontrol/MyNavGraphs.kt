package com.example.opencontrol

import com.example.opencontrol.view.NavGraph
import com.example.opencontrol.view.destinations.ChatTabDestination
import com.example.opencontrol.view.destinations.HomeTabDestination
import com.example.opencontrol.view.destinations.LoginScreenDestination
import com.example.opencontrol.view.destinations.MainScreenDestination
import com.example.opencontrol.view.destinations.NewNoteDestination
import com.example.opencontrol.view.destinations.NoteInfoDestination
import com.example.opencontrol.view.destinations.NoteTabDestination
import com.example.opencontrol.view.destinations.UserTabDestination

object MyNavGraphs {
    val baseGraph = NavGraph(
        route = "root",
        startRoute = LoginScreenDestination,
        destinations = listOf(
            LoginScreenDestination,
            MainScreenDestination,
        )
    )
    val mainGraph = NavGraph(
        route = "root",
        startRoute = HomeTabDestination,
        destinations = listOf(
            ChatTabDestination,
            HomeTabDestination,
            UserTabDestination,
            NoteTabDestination,
            NoteInfoDestination,
            NewNoteDestination
        )
    )
}