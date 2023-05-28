package com.example.opencontrol.view.homeTab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.R
import com.example.opencontrol.ui.theme.LightGrey
import com.example.opencontrol.ui.theme.OrangeBackground
import com.example.opencontrol.view.enterScreen.MoscowLogo
import com.example.opencontrol.view.enterScreen.OpenControlLogo
import com.example.opencontrol.view.noteTab.NoteInfoScreen
import org.koin.androidx.compose.getViewModel
import timber.log.Timber
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeScreenContent()
    }
}

@Composable
private fun HomeScreenContent() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<MainViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        OpenControlLogo()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(OrangeBackground)
        ) {
            item { MoscowLogo() }
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 16.dp)
                ) {
                    WidgetButton(modifier = Modifier
                        .padding(8.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12))
                        .background(LightGrey)
                        .clickable {
                            Timber.d("@@@ Калькулятор нарушений")
                        }
                        .weight(1f),
                        text = "Калькулятор нарушений",
                        icon = R.drawable.calculator_icon)
                    val nearestNote = viewModel.getNearestNote()
                    val formatter = DateTimeFormatter.ofPattern("dd MMMM", Locale("ru"))
                    Timber.d("@@@ nearestNote = $nearestNote")
                    val text = if (nearestNote != null)
                        "Ближайшая запись: ${nearestNote.date.format(formatter)} ${nearestNote.time}"
                    else "Нет ближайших записей"
                    WidgetButton(text = text,
                        modifier = Modifier
                            .padding(8.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12))
                            .background(LightGrey)
                            .clickable {
                                if (nearestNote != null)
                                    navigator.push(NoteInfoScreen(nearestNote.id))
                            }
                            .weight(1f),
                        icon = R.drawable.calendar_icon)
                }
            }
            item { Banner() }
        }
    }
}

@Composable
private fun WidgetButton(text: String, modifier: Modifier, icon: Int) {
    Box(
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "", modifier = Modifier.padding(top = 24.dp, end = 24.dp)
                )
            }
            Text(
                text = text,
                modifier = Modifier.padding(start = 14.dp, bottom = 24.dp)
            )
        }
    }
}

@Composable
private fun Banner() {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "Одна платформа для решения вопросов с проверками бизнеса",
            fontSize = 23.sp,
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = "Сервис взаимодействия бизнеса с органами контроля",
            fontSize = 14.sp,
            modifier = Modifier
                .padding(8.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.person_sergey_1),
            contentDescription = "", modifier = Modifier
                .width(screenWidthDp - 16.dp)
                .height(screenWidthDp - 16.dp)
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}
