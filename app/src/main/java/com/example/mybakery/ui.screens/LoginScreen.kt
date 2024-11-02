package com.example.mybakery.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mybakery.data.model.LoginCredentials
import com.example.mybakery.data.model.LoginResponse
import com.example.mybakery.repository.AuthRepository
import com.example.mybakery.ui.screens.components.LoginForm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(authRepository: AuthRepository, onLoginSuccess: (LoginResponse) -> Unit) {
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Título de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))

        // Componente del formulario de inicio de sesión
        LoginForm(
            onLogin = { credentials ->
                Log.d("LoginScreen", "Iniciando sesión para el usuario: ${credentials.email}")

                isLoading = true
                errorMessage = ""
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = authRepository.login(credentials.email, credentials.password)
                        // Llama a la función de éxito con la respuesta del login
                        onLoginSuccess(response)
                    } catch (e: Exception) {
                        // Manejo de errores
                        errorMessage = when {
                            e.message?.contains("Invalid credentials") == true -> "Credenciales inválidas."
                            e.message?.contains("Email not verified") == true -> "El correo no está verificado."
                            e.message?.contains("User account is inactive") == true -> "La cuenta de usuario está inactiva."
                            else -> "Error inesperado: ${e.message}"
                        }
                        Log.e("LoginScreen", "Error en el inicio de sesión: ${e.message}")

                    } finally {
                        isLoading = false
                    }
                }
            },
            isLoading = isLoading,
            errorMessage = errorMessage
        )
    }
}
