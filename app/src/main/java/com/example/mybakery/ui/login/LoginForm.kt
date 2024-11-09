package com.example.mybakery.ui.login

import androidx.compose.runtime.*
import com.example.mybakery.data.model.LoginCredentials

@Composable
fun LoginForm(onLogin: (LoginCredentials) -> Unit, isLoading: Boolean, errorMessage: String) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

}
