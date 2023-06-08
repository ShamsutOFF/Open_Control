package com.example.opencontrol.view.userProfileTab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.model.networkDTOs.UserInfoNetwork
import com.example.opencontrol.view.EndEditingBlock
import com.example.opencontrol.view.EnterNumberInfoItemBlock
import com.example.opencontrol.view.EnterTextInfoItemBlock
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
    viewModel.getUserInfo()
    val navigator = LocalNavigator.currentOrThrow
    var lastName by remember { mutableStateOf(viewModel.userInfo.lastName ?: "") }
    var firstName by remember { mutableStateOf(viewModel.userInfo.firstName ?: "") }
    var surName by remember { mutableStateOf(viewModel.userInfo.surName ?: "") }
    var email by remember { mutableStateOf(viewModel.userInfo.email ?: "") }
    var inn by remember { mutableStateOf(viewModel.userInfo.inn ?: 0L) }
    var snils by remember { mutableStateOf(viewModel.userInfo.snils ?: 0L) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderBlock("Сведения о пользователе")
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { EnterTextInfoItemBlock("Фамилия", "Фамилия", lastName) { lastName = it } }
            item { EnterTextInfoItemBlock("Имя", "Имя", firstName) { firstName = it } }
            item { EnterTextInfoItemBlock("Отчество", "Отчество", surName) { surName = it } }

            item { EnterNumberInfoItemBlock("ИНН", "ИНН", inn) { inn = it } }

            item { EnterNumberInfoItemBlock("СНИЛС", "СНИЛС", snils) { snils = it } }

            item { EnterTextInfoItemBlock("Email", "Email", email) { email = it } }

            item {
                EndEditingBlock(
                    textOnConfirm = "Сохранить",
                    onConfirm = {
                        Timber.d("@@@ onConfirm")
                        Timber.d("@@@ firstName = $firstName")
                        viewModel.saveUserInfo(
                            UserInfoNetwork(
                                userId = viewModel.userId,
                                email = email,
                                firstName = firstName,
                                lastName = lastName,
                                surName = surName,
                                inn = inn,
                                snils = snils
                            )
                        )
                        navigator.pop()
                    },
                    textOnDismiss = "Отменить",
                    onDismiss = {
                        navigator.pop()
                        Timber.d("@@@ onDismiss")
                    },
                )
            }
        }
    }
}