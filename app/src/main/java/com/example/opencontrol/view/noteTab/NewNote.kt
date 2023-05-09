package com.example.opencontrol.view.noteTab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opencontrol.MainViewModel
import com.example.opencontrol.ui.theme.Typography
import com.example.opencontrol.ui.theme.md_theme_light_primary
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

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
            item { SelectableItem("Kонтрольно-надзорный орган","выберите орган контроля") }
            item { SelectableItem("Вид контроля","выберите вид контроля") }
            item { SelectableItem("Подразделение","выберите подразделение") }
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
private fun SelectableItem(title: String, initialText: String) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(initialText) }
    Column(modifier = Modifier
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
            DropdownMenuItem(
                onClick = {
                    selectedOption = "Copy"
                    expanded = false
                },
                text = { Text("Скопировать") })
            Divider()
            DropdownMenuItem(
                onClick = {
                    selectedOption = "Paste"
                    expanded = false
                },
                text = { Text("Вставить") })
            Divider()
            DropdownMenuItem(
                onClick = {
                    selectedOption = "Settings"
                    expanded = false
                },
                text = { Text("Настройки") })
        }
    }
}