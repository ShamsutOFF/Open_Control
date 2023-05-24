package com.example.opencontrol.view.enterScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.opencontrol.ui.theme.Invisible
import com.example.opencontrol.ui.theme.OrangeMainTransparent73
import com.example.opencontrol.ui.theme.md_theme_light_primary
import timber.log.Timber

class RegistrationScreen :Screen {
    @Composable
    override fun Content() {
        RegistrationScreenContent()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RegistrationScreenContent() {
    val navigator = LocalNavigator.currentOrThrow
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.Center, modifier = Modifier
            .padding(horizontal = 32.dp)
    ) {
        LoginTextField(
            login,
            { login = it },
            keyboardController,
            focusManager
        )
        PasswordTextField(
            password,
            { password = it },
            keyboardController,
            focusManager,
            passwordVisible,
            { passwordVisible = it }
        )
        HaveAccountTextButton()
        BigSquareButton()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
private fun LoginTextField(
    login: String,
    setLoginValue: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager
) {
    TextField(
        value = login,
        label = { Text(text = "Email", fontWeight = FontWeight.SemiBold) },
        textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
        onValueChange = { setLoginValue(it) },
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            focusManager.clearFocus()
        }),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = md_theme_light_primary,
            unfocusedLabelColor = OrangeMainTransparent73,
            cursorColor = md_theme_light_primary,
            containerColor = Invisible,
            textColor = md_theme_light_primary,
            selectionColors = TextSelectionColors(md_theme_light_primary, md_theme_light_primary),
            focusedIndicatorColor = md_theme_light_primary,
            unfocusedIndicatorColor = OrangeMainTransparent73,
        )
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
private fun PasswordTextField(
    password: String,
    setPassNewValue: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    passwordVisibility: Boolean,
    setPassVisibility: (Boolean) -> Unit
) {
    TextField(
        value = password,
        label = { Text(text = "пароль", fontWeight = FontWeight.SemiBold) },
        textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
        onValueChange = { setPassNewValue(it) },
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            focusManager.clearFocus()
        }),
        singleLine = true,
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisibility)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff
            val description = if (passwordVisibility) "Hide password" else "Show password"
            IconButton(onClick = { setPassVisibility(!passwordVisibility) }) {
                Icon(imageVector = image, description)
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = md_theme_light_primary,
            unfocusedLabelColor = OrangeMainTransparent73,
            focusedTrailingIconColor = md_theme_light_primary,
            unfocusedTrailingIconColor = OrangeMainTransparent73,
            cursorColor = md_theme_light_primary,
            containerColor = Invisible,
            textColor = md_theme_light_primary,
            selectionColors = TextSelectionColors(md_theme_light_primary, md_theme_light_primary),
            focusedIndicatorColor = md_theme_light_primary,
            unfocusedIndicatorColor = OrangeMainTransparent73,
        )
    )
}

@Composable
private fun HaveAccountTextButton() {
    val navigator = LocalNavigator.currentOrThrow
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Text(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clickable {
                    navigator.push(LoginScreen())
                    Timber.d("@@@ Click to Уже есть аккаунт?") },
            text = "Уже есть аккаунт?",
            fontWeight = FontWeight.SemiBold,
            color = md_theme_light_primary,
            fontSize = 13.sp,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
private fun BigSquareButton() {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .height(60.dp),
        onClick = {
            Timber.d("@@@ Click to Зарегестрироваться!!!")
        },
        shape = RoundedCornerShape(32),
        colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_primary)
    ) {
        Text(
            text = "Зарегестрироваться",
            modifier = Modifier
                .padding(8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}