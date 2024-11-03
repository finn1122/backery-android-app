package com.example.mybakery.ui.screens.components.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybakery.data.model.LoginResponse
import com.example.mybakery.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
    authRepository: AuthRepository,
    onLoginSuccess: (LoginResponse) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(32.dp))

        LoginForm(
            onLogin = { credentials ->
                Log.d("LoginScreen", "Iniciando sesión para el usuario: ${credentials.email}")

                isLoading = true
                errorMessage = ""

                scope.launch(Dispatchers.IO) {
                    try {
                        val response = authRepository.login(credentials.email, credentials.password)

                        if (response != null) {
                            withContext(Dispatchers.Main) {
                                onLoginSuccess(response)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                errorMessage = "Error inesperado al iniciar sesión."
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            errorMessage = when {
                                e.message?.contains("Invalid credentials") == true -> "Credenciales inválidas."
                                e.message?.contains("Email not verified") == true -> "El correo no está verificado."
                                e.message?.contains("User account is inactive") == true -> "La cuenta de usuario está inactiva."
                                else -> "Error inesperado: ${e.message}"
                            }
                            Log.e("LoginScreen", "Error en el inicio de sesión: ${e.message}")
                        }
                    } finally {
                        withContext(Dispatchers.Main) {
                            isLoading = false
                        }
                    }
                }
            },
            isLoading = isLoading,
            errorMessage = errorMessage
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿No tienes una cuenta? Regístrate",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                onNavigateToRegister()
            }
        )
    }
}
