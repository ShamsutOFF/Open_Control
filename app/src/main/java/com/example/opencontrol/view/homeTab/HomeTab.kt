package com.example.opencontrol.view.homeTab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.R
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

object HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Home"
            val icon =
                rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.home_icon))
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        HomeTabContent()
    }
}

@Composable
private fun HomeTabContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val viewModel = getViewModel<MainViewModel>()
        Timber.d("@@@ HomeTab viewModelId = ${viewModel.viewModelId}")
        Text(text = "HomeTab")
    }
}