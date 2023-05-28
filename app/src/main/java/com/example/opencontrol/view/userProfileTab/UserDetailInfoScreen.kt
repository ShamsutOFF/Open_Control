package com.example.opencontrol.view.userProfileTab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.view.EndEditingBlock
import com.example.opencontrol.view.EnterInfoItemBlock
import com.example.opencontrol.view.HeaderBlock
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

class UserDetailInfoScreen : Screen {
    @Composable
    override fun Content() {
        UserDetailInfoScreenContent()
    }
}

@Composable
private fun UserDetailInfoScreenContent() {
    val viewModel = getViewModel<MainViewModel>()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderBlock("Сведения о пользователе")
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { EnterInfoItemBlock("Фамилия", "Фамилия") }
            item { EnterInfoItemBlock("Имя", "Имя") }
            item { EnterInfoItemBlock("Отчество", "Отчество") }
            item { EnterInfoItemBlock("Пол", "Пол") }
            item { EnterInfoItemBlock("ИНН", "ИНН") }
            item { EnterInfoItemBlock("СНИЛС", "СНИЛС") }

            item { EnterInfoItemBlock("Телефон", "Телефон") }
            item { EnterInfoItemBlock("Почта", "Почта") }

            item { EnterInfoItemBlock("Дата рождения", "Дата рождения") }
            item { EnterInfoItemBlock("Серия", "Серия") }
            item { EnterInfoItemBlock("Номер", "Номер") }
            item { EnterInfoItemBlock("Кем выдан", "Кем выдан") }
            item { EnterInfoItemBlock("Дата выдачи", "Дата выдачи") }
            item { EnterInfoItemBlock("Адрес регистрации", "Адрес регистрации") }
            item {
                EndEditingBlock(
                    onDismiss = { Timber.d("@@@ onDismiss") },
                    onConfirm = { Timber.d("@@@ onConfirm") })
            }
        }
    }
}