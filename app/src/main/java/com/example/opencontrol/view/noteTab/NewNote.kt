package com.example.opencontrol.view.noteTab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.ui.theme.Typography
import com.example.opencontrol.ui.theme.md_theme_light_primary
import com.example.opencontrol.ui.theme.md_theme_light_secondary
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

@Destination
@Composable
fun NewNote(navigator: DestinationsNavigator) {
    val viewModel = getViewModel<MainViewModel>()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderBlock()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { SelectableItem("Kонтрольно-надзорный орган", viewModel.getControlAgencies()) }
            item { SelectableItem("Вид контроля", viewModel.getControlTypes()) }
            item { SelectableItem("Подразделение", viewModel.getDepartments()) }
            item { ToggleItem("Выберите тип встречи") }
            item { ExtraInfoItem("Дополнительная информация") }
        }
    }
}

@Composable
private fun HeaderBlock() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var width by remember { mutableStateOf(Int.MIN_VALUE) }
        val widthText = LocalDensity.current.run { width.toDp() }

        Box(Modifier.onGloballyPositioned { coordinates ->
            width = coordinates.size.width
        }) {
            Text(text = "Новая запись", style = Typography.headlineMedium)
        }
        Divider(
            color = md_theme_light_primary,
            thickness = 3.dp,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .width(widthText)
        )
    }
}

@Composable
private fun SelectableItem(title: String, values: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("нажмите для выбора") }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = title, fontSize = 14.sp)
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { expanded = true }
        ) {
            Text(text = selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            values.forEachIndexed { index, it ->
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = {
                        selectedOption = it
                        expanded = false
                    })
                if (index != values.size - 1) Divider()
            }
        }
    }
}

@Composable
private fun ToggleItem(title: String) {
    var checkedfirst by remember { mutableStateOf(false) }
    var checkedSecond by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = title, fontSize = 14.sp)
        Row(modifier = Modifier.fillMaxWidth()) {
            FilledIconToggleButton(
//            FilledTonalIconToggleButton(
                checked = checkedfirst,
                onCheckedChange = {
                    if (!checkedfirst) {
                        checkedfirst = true
                        checkedSecond = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = "ВИДЕО-КОНФЕРЕНЦИЯ", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            FilledIconToggleButton(
                checked = checkedSecond,
                onCheckedChange = {
                    if (!checkedSecond) {
                        checkedSecond = true
                        checkedfirst = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = "ЛИЧНЫЙ ВИЗИТ", fontSize = 14.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class
)
@Composable
private fun ExtraInfoItem(title: String) {
    var extraText by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = title, fontSize = 14.sp)
        OutlinedTextField(
            value = extraText,
            label = { Text(text = "Комментарий к проверке")},
            onValueChange = { extraText = it },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
//                .clearFocusOnKeyboardDismiss(),
            shape = androidx.compose.foundation.shape.CircleShape,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                })
        )
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Modifier.clearFocusOnKeyboardDismiss() = composed{
    var isFocused by remember { mutableStateOf(false) }
    Timber.d("@@@ isFocused = $isFocused")
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    Timber.d("@@@ 1 keyboardAppearedSinceLastFocused = $isFocused")
    if (isFocused) {
        val imeIsVisible = WindowInsets.isImeVisible
        val focusManager = LocalFocusManager.current

        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                Timber.d("@@@ 2 keyboardAppearedSinceLastFocused = $isFocused")
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        Timber.d("@@@ onFocusEvent ")
        Timber.d("@@@ 1 isFocused = $isFocused")
        Timber.d("@@@ it.isFocused = ${it.isFocused}")
        if (isFocused != it.isFocused) {
            Timber.d("@@@ 2 isFocused = $isFocused")
            isFocused = it.isFocused
            Timber.d("@@@ 3 isFocused = $isFocused")
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}