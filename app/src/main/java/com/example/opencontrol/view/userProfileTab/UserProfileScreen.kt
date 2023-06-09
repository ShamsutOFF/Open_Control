package com.example.opencontrol.view.userProfileTab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.R
import com.example.opencontrol.model.UserRole
import com.example.opencontrol.ui.theme.ExitIconBackground
import com.example.opencontrol.ui.theme.ExitIconIcon
import com.example.opencontrol.ui.theme.LightColors
import com.example.opencontrol.ui.theme.LightGrey
import com.example.opencontrol.ui.theme.LightYellowIcon
import com.example.opencontrol.ui.theme.OrangeBackground
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

class UserProfileScreen : Screen {
    @Composable
    override fun Content() {
        UserProfileScreenContent()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun UserProfileScreenContent() {
    val viewModel = getViewModel<MainViewModel>()

    if (viewModel.userRole == UserRole.BUSINESS) {
        viewModel.getBusinessUserInfo()
    } else {
        viewModel.getInspectorUserInfo()
    }
    val refreshing by remember { mutableStateOf(false) }
    val state = rememberPullRefreshState(refreshing, {
        if (viewModel.userRole == UserRole.BUSINESS) {
            viewModel.getBusinessUserInfo()
        } else {
            viewModel.getInspectorUserInfo()
        }
    })

    Box(Modifier.pullRefresh(state)) {
        LazyColumn() {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(OrangeBackground)
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserPhotoFrame()
                    if (viewModel.userRole == UserRole.BUSINESS) {
                        UserName("${viewModel.businessUserInfo.firstName ?: ""} ${viewModel.businessUserInfo.lastName ?: "Заполните ФИО"} ")
                        EmailChip(viewModel.businessUserInfo.email ?: "Заполните Email")
                    } else {
                        UserName("${viewModel.inspectorUserInfo.firstName ?: ""} ${viewModel.inspectorUserInfo.lastName ?: "Заполните ФИО"} ")
                        EmailChip(viewModel.inspectorUserInfo.email ?: "Заполните Email")
                    }
                }
            }
            if (viewModel.userRole == UserRole.BUSINESS) {
                item { BusinessBlock() }
                item { ViolationsBlock() }
                item { AddBusinessButton() }
            }
            item { UserDetailInfoButton() }
            item { AccountExitButton() }
        }
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }

}

@Composable
private fun UserPhotoFrame() {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(screenWidthDp / 2)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier
            .padding(4.dp)
            .width(screenWidthDp / 3)
            .clip(CircleShape)
            .background(LightColors.primary.copy(alpha = 0.40f))
            .border(
                width = 7.dp,
                color = LightColors.onPrimary,
                shape = CircleShape
            )
            .clickable { Timber.d("@@@ Click to photoFrame") }
            .aspectRatio(1f)
        )
        Box(modifier = Modifier
            .offset(y = 60.dp)
            .padding(4.dp)
            .width(screenWidthDp / 9)
            .clip(CircleShape)
            .background(LightColors.primary)
            .border(
                width = 5.dp,
                color = LightColors.onPrimary,
                shape = CircleShape
            )
            .clickable { Timber.d("@@@ Click to pencil_icon") }
            .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.pencil_icon),
                contentDescription = "",
                modifier = Modifier.width(screenWidthDp / 10)
            )
        }
    }
}

@Composable
private fun UserName(name: String) {
    Text(
        text = name,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
private fun EmailChip(email: String) {
    Text(
        text = email,
        fontSize = 12.sp,
        modifier = Modifier
            .background(LightColors.onPrimary, shape = RoundedCornerShape(50))
            .padding(vertical = 8.dp, horizontal = 16.dp)
    )
}

@Composable
private fun BusinessBlock() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Ваш бизнес:",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow() {
            items(5) { BusinessCard() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BusinessCard() {
    val navigator = LocalNavigator.currentOrThrow
    Card(
        onClick = { Timber.d("@@@ Click to BusinessCard") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = LightGrey),
    ) {
        Text(
            text = "ООО \"Моя Кафешка\"",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 8.dp)
        )
        Text(
            text = "г. Москва, улица Садовая дом 28 к5",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 32.dp)
        )
    }
}

@Composable
private fun ViolationsBlock() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Вероятность нарушений:",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow() {
            items(5) { ViolationsCard() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViolationsCard() {
    val navigator = LocalNavigator.currentOrThrow
    Card(
        onClick = { Timber.d("@@@ Click to ViolationsCard") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = LightGrey),
    ) {
        Text(
            text = "ООО \"Моя Кафешка\"",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        )
        Text(
            text = "г. Москва, улица Садовая дом 28 к5",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 4.dp)
        )
        Text(
            text = "Еще какая-то инфа",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 4.dp)
        )
        Text(
            text = "Возможное колличество нарушений: 5",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
        )
    }
}

@Composable
private fun AddBusinessButton() {
    val navigator = LocalNavigator.currentOrThrow
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { navigator.push(AddBusinessScreen()) }
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .width(screenWidthDp / 9)
                .clip(CircleShape)
                .background(LightYellowIcon)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Add, null, tint = LightColors.primary)
        }
        Text(
            text = "Добавить бизнес",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(Icons.Filled.KeyboardArrowRight, null)
        }
    }
}

@Composable
private fun UserDetailInfoButton() {
    val navigator = LocalNavigator.currentOrThrow
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { navigator.push(UserDetailInfoScreen()) }
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .width(screenWidthDp / 9)
                .clip(CircleShape)
                .background(LightYellowIcon)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(id = R.drawable.pencil_icon), null, tint = LightColors.primary)
        }
        Text(
            text = "Сведения о пользователе",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(Icons.Filled.KeyboardArrowRight, null)
        }
    }
}

@Composable
private fun AccountExitButton() {
    val viewModel = getViewModel<MainViewModel>()
    val navigator = LocalNavigator.currentOrThrow
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                viewModel.clearUser()
                navigator.popUntilRoot()
            }
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .width(screenWidthDp / 9)
                .clip(CircleShape)
                .background(ExitIconBackground)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.ExitToApp, null, tint = ExitIconIcon)
        }
        Text(
            text = "Выход",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(Icons.Filled.KeyboardArrowRight, null)
        }
    }
}
