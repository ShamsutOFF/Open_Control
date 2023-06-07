package com.example.opencontrol.view.noteTab

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.opencontrol.R

object NoteTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Note"
            val icon =
                rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.note_icon))
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        BottomSheetNavigator {
            Navigator(screen = NoteScreen()) { navigator ->
                SlideTransition(navigator = navigator) {
                    it.Content()
                }
            }
        }
    }
}