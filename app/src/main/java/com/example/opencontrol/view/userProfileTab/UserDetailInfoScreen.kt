package com.example.opencontrol.view.userProfileTab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.opencontrol.model.UserRole
import com.example.opencontrol.model.networkDTOs.BusinessUserInfoNetwork
import com.example.opencontrol.model.networkDTOs.InspectorUserInfoNetwork
import com.example.opencontrol.view.EndEditingBlock
import com.example.opencontrol.view.EnterNumberInfoItemBlock
import com.example.opencontrol.view.EnterTextInfoItemBlock
import com.example.opencontrol.view.HeaderBlock
import com.example.opencontrol.view.SelectableItemBlock
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

    if (viewModel.userRole == UserRole.BUSINESS) {
        viewModel.getBusinessUserInfo()
    } else {
        viewModel.getInspectorUserInfo()
    }

    val navigator = LocalNavigator.currentOrThrow

    var lastName by remember {
        mutableStateOf(
            if (viewModel.userRole == UserRole.BUSINESS) {
                viewModel.businessUserInfo.lastName ?: ""
            } else {
                viewModel.inspectorUserInfo.lastName ?: ""
            }
        )
    }
    var firstName by remember {
        mutableStateOf(
            if (viewModel.userRole == UserRole.BUSINESS) {
                viewModel.businessUserInfo.firstName ?: ""
            } else {
                viewModel.inspectorUserInfo.firstName ?: ""
            }
        )
    }
    var surName by remember {
        mutableStateOf(
            if (viewModel.userRole == UserRole.BUSINESS) {
                viewModel.businessUserInfo.surName ?: ""
            } else {
                viewModel.inspectorUserInfo.surName ?: ""
            }
        )
    }
    var email by remember {
        mutableStateOf(
            if (viewModel.userRole == UserRole.BUSINESS) {
                viewModel.businessUserInfo.email ?: ""
            } else {
                viewModel.inspectorUserInfo.email ?: ""
            }
        )
    }

    var inn by remember { mutableStateOf(viewModel.businessUserInfo.inn ?: 0L) }
    var snils by remember { mutableStateOf(viewModel.businessUserInfo.snils ?: 0L) }
    var inspectorsKnoId by remember { mutableStateOf(viewModel.inspectorKnoId) }
    val knosNames = viewModel.listOfAllKnos.map {
        it.name
    }
    var selectedKno by remember {
        mutableStateOf("нажмите для выбора")
    }
    LaunchedEffect(key1 = true) {
        Timber.d("@@@ LaunchedEffect(key1 = true)")
        val kno = viewModel.getKnoById(viewModel.inspectorKnoId)
        if (kno != null) {
            Timber.d("@@@ LaunchedEffect(kno.name = ${kno.name})")
            selectedKno = kno.name
        }
    }
    LaunchedEffect(key1 = selectedKno) {
        val kno = viewModel.getKnoByName(selectedKno)
        if (kno != null) {
            inspectorsKnoId = kno.id
        }
    }

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
            if (viewModel.userRole == UserRole.BUSINESS) {
                item { EnterNumberInfoItemBlock("ИНН", "ИНН", inn) { inn = it } }
                item { EnterNumberInfoItemBlock("СНИЛС", "СНИЛС", snils) { snils = it } }
            } else {
                item {
                    SelectableItemBlock("Kонтрольно-надзорный орган", knosNames, selectedKno) {
                        Timber.d("@@@ selected = $it")
                        selectedKno = it
                    }
                }
            }
            item { EnterTextInfoItemBlock("Email", "Email", email) { email = it } }

            item {
                EndEditingBlock(
                    textOnConfirm = "Сохранить",
                    onConfirm = {
                        Timber.d("@@@ onConfirm")
                        Timber.d("@@@ firstName = $firstName")
                        if (viewModel.userRole == UserRole.BUSINESS) {
                            viewModel.saveBusinessUserInfo(
                                BusinessUserInfoNetwork(
                                    userId = viewModel.userId,
                                    email = email,
                                    firstName = firstName,
                                    lastName = lastName,
                                    surName = surName,
                                    inn = inn,
                                    snils = snils
                                )
                            )
                        } else {
                            viewModel.saveInspectorUserInfo(
                                InspectorUserInfoNetwork(
                                    userId = viewModel.userId,
                                    email = email,
                                    firstName = firstName,
                                    lastName = lastName,
                                    surName = surName,
                                    knoId = inspectorsKnoId
                                )
                            )
                        }
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