package com.example.opencontrol.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opencontrol.ui.theme.GreyCancelButton
import com.example.opencontrol.ui.theme.GreyText
import com.example.opencontrol.ui.theme.LightColors
import com.example.opencontrol.ui.theme.Typography
import com.example.opencontrol.ui.theme.md_theme_light_primary

@Composable
fun HeaderBlock(title: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var width by remember { mutableStateOf(Int.MIN_VALUE) }
        val widthText = LocalDensity.current.run { width.toDp() }

        Box(Modifier.onGloballyPositioned { coordinates ->
            width = coordinates.size.width
        }) {
            Text(text = title, style = Typography.headlineMedium)
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
fun SelectableItemBlock(
    title: String,
    values: List<String>,
    selectedText: String,
    selected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = title, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(32),
            onClick = { expanded = true },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = GreyText)
        ) {
            Text(text = selectedText)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            values.forEachIndexed { index, it ->
                DropdownMenuItem(
                    contentPadding = PaddingValues(4.dp),
                    modifier = Modifier
                        .padding(4.dp),
                    text = { Text(text = it) },
                    onClick = {
                        selected(it)
                        expanded = false
                    })
                if (index != values.size - 1) Divider()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EnterInfoItemBlock(title: String, label: String) {
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
            label = { Text(text = label) },
            onValueChange = { extraText = it },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(32),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            })
        )
    }
}

@Composable
fun EndEditingBlock(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(8.dp)
        NegativeButton(
            text = "Отменить",
            modifier = modifier,
            onClick = onDismiss
        )
        PositiveButton(
            text = "Применить",
            modifier = modifier,
            onClick = onConfirm
        )
    }
}

@Composable
private fun PositiveButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(32),
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun NegativeButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(32),
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = GreyCancelButton,
            contentColor = LightColors.primary
        )
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}