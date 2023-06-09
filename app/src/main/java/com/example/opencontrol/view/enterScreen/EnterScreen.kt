package com.example.opencontrol.view.enterScreen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.opencontrol.R
import com.example.opencontrol.model.UserRole
import com.example.opencontrol.ui.theme.OrangeBackground
import com.example.opencontrol.ui.theme.md_theme_light_onPrimary
import com.example.opencontrol.ui.theme.md_theme_light_primary

class EnterScreen : Screen {
    @Composable
    override fun Content() {
        EnterScreenContent()
    }
}

@Composable
private fun EnterScreenContent() {
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
                EnterButton(modifier = Modifier
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12))
                    .background(md_theme_light_primary)
                    .clickable {
                        navigator.push(LoginScreen(UserRole.BUSINESS))
                    }
                    .weight(1f),
                    text = "КАК БИЗНЕС")

                EnterButton(modifier = Modifier
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12))
                    .background(md_theme_light_primary)
                    .clickable {
                        navigator.push(LoginScreen(UserRole.INSPECTION))
                    }
                    .weight(1f),
                    text = "КАК ИНСПЕКТОР")
            }
        }
    }
}

@Composable
fun OpenControlLogo() {
    Image(
        painter = painterResource(id = R.drawable.open_control_logo),
        contentDescription = "Лого", modifier = Modifier.padding(start = 28.dp)
    )
}

@Composable
fun MoscowLogo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Image(
            painter = painterResource(id = R.drawable.mos_logo),
            contentDescription = "",
            modifier = Modifier.padding(start = 24.dp).size(70.dp)
        )
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(text = "При поддержке", fontWeight = FontWeight.Bold)
            Text(text = "Правительства Москвы", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EnterButton(text: String, modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            Text(
                text = "Войти",
                color = md_theme_light_onPrimary,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = text,
                color = md_theme_light_onPrimary,
                modifier = Modifier.padding(start = 16.dp, bottom = 24.dp)
            )
        }
    }
}