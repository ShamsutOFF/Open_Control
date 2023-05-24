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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.opencontrol.R
import com.example.opencontrol.ui.theme.LightGreyButton
import com.example.opencontrol.ui.theme.OrangeBackground
import com.example.opencontrol.view.enterScreen.MoscowLogo
import com.example.opencontrol.view.enterScreen.OpenControlLogo
import timber.log.Timber

class HomeScreen: Screen{
    @Composable
    override fun Content() {
        HomeScreenContent()
    }
}

@Composable
private fun HomeScreenContent() {
    val navigator = LocalNavigator.currentOrThrow
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        OpenControlLogo()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(OrangeBackground)
        ) {
            MoscowLogo()
            Row(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 16.dp)
            ) {
                WidgetButton(modifier = Modifier
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12))
                    .background(LightGreyButton)
                    .clickable {
                        Timber.d("@@@ Калькулятор нарушений")
//                        navigator.push(LoginScreen())
                    }
                    .weight(1f),
                    text = "Калькулятор нарушений",
                    icon = R.drawable.calculator_icon)

                WidgetButton(text = "Ближайшая запись: 18 мая 12:00 департамент...",
                    modifier = Modifier
                        .padding(8.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12))
                        .background(LightGreyButton)
                        .clickable {
                            Timber.d("@@@ Ближайшая запись: 18 мая 12:00 департамент...")
//                        navigator.push(LoginScreen())
                        }
                        .weight(1f),
                    icon = R.drawable.calendar_icon)
            }
            Banner()
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
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}