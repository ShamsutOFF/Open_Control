package com.example.opencontrol.view.userProfileTab

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.opencontrol.R

object UserProfileTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "User"
            val icon =
                rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.user_icon))
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    override fun Content() {
        Navigator(screen = UserProfileScreen()) { navigator ->
            SlideTransition(navigator = navigator) {
                it.Content()
            }
        }
    }
}