package com.example.mybakery.ui.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.mybakery.data.model.LoginResponse
import com.example.mybakery.repository.AuthRepository

@Composable
fun LoginScreen(
    authRepository: AuthRepository,
    onLoginSuccess: (LoginResponse) -> Unit,
    onAdminBakeryCheck: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                errorMessage = ""
                scope.launch(Dispatchers.Main) {
                    try {
                        val loginResult = withContext(Dispatchers.IO) {
                            authRepository.login(email, password)
                        }
                        loginResult.onSuccess { loginResponse ->
                            val userRole = loginResponse.user.roles.firstOrNull()
                            if (userRole != null) {
                                Toast.makeText(context, "Rol del usuario: $userRole", Toast.LENGTH_SHORT).show()
                            }

                            onLoginSuccess(loginResponse)

                            if (userRole == "admin") {
                                val bakeryResult = withContext(Dispatchers.IO) {
                                    authRepository.verifyBakery()
                                }
                                bakeryResult.onSuccess {
                                    Toast.makeText(context, "Admin ha iniciado sesión", Toast.LENGTH_SHORT).show()
                                    onAdminBakeryCheck()
                                }.onFailure { e: Throwable ->
                                    errorMessage = e.message ?: "Error desconocido en la verificación de la panadería"
                                    Log.e("LoginScreen", "Error en la verificación de la panadería: ${e.message}")
                                }
                            }
                        }.onFailure { e: Throwable ->
                            errorMessage = e.message ?: "Error desconocido"
                            Log.e("LoginScreen", "Error en el login: ${e.message}")
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Error desconocido"
                        Log.e("LoginScreen", "Error en la operación: ${e.message}")
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Login")
            }
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes una cuenta? Regístrate")
        }
    }
}
