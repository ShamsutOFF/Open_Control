package com.example.opencontrol.view.userProfileTab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.view.EndEditingBlock
import com.example.opencontrol.view.EnterTextInfoItemBlock
import com.example.opencontrol.view.HeaderBlock
import com.example.opencontrol.view.SelectableItemBlock
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

class AddBusinessScreen : Screen {
    @Composable
    override fun Content() {
        AddBusinessScreenContent()
    }
}

@Composable
private fun AddBusinessScreenContent() {
    val viewModel = getViewModel<MainViewModel>()
    var businessName by remember { mutableStateOf("Наименование бизнеса") }
    var typeOfActivity by remember { mutableStateOf("Вид деятельности") }
    var classification by remember { mutableStateOf("Классификация профиля деятельности") }
    var legalAddress by remember { mutableStateOf("Юридический адрес") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderBlock("Добавить бизнес")
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Добавьте информацию о вашем бизнесе и вы получите доступ ко всем функциям системы",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }
            item { EnterTextInfoItemBlock(
                "Наименование бизнеса",
                "Введите название бизнеса",
                businessName
            ) { businessName = it }
            }
            item {
                SelectableItemBlock(
                    title = "Тип цифрового профиля объекта",
                    values = listOf(
                        "Тип цифрового профиля объекта 1",
                        "Тип цифрового профиля объекта 2",
                        "Тип цифрового профиля объекта 3"
                    ), "нажмите для выбора"
                ) {
                    Timber.d("@@@ selected = $it")
                }
            }
            item { EnterTextInfoItemBlock(
                "Вид деятельности",
                "Введите вид деятельности",
                typeOfActivity
            ) { typeOfActivity = it }
            }
            item {
                SelectableItemBlock(
                    title = "Классификация объекта",
                    values = listOf(
                        "Классификация объекта 1",
                        "Классификация объекта 2",
                        "Классификация объекта 3"
                    ), "нажмите для выбора"
                ) {
                    Timber.d("@@@ selected = $it")
                }
            }
            item {
                EnterTextInfoItemBlock(
                    "Классификация профиля деятельности",
                    "Введите классификацию",
                    classification
                ) { classification = it }
            }
            item { EnterTextInfoItemBlock(
                "Юридический адрес",
                "Введите юридический адрес",
                legalAddress
            ) { legalAddress = it }
            }
            item {
                EndEditingBlock(
                    textOnConfirm = "Применить",
                    onDismiss = { Timber.d("@@@ onDismiss") },
                    textOnDismiss = "Отменить",
                    onConfirm = { Timber.d("@@@ onConfirm") })
            }
        }
    }
}