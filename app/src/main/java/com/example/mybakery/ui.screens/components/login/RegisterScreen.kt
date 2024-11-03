package com.example.mybakery.ui.screens.components.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.mybakery.repository.AuthRepository

@Composable
fun RegisterScreen(
    authRepository: AuthRepository,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isRegisterSuccess by remember { mutableStateOf(false) }
    var canResendEmail by remember { mutableStateOf(false) }
    var timer by remember { mutableStateOf(60) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isRegisterSuccess) {
        if (isRegisterSuccess) {
            canResendEmail = false
            while (timer > 0) {
                delay(1000L)
                timer--
            }
            canResendEmail = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Register", style = MaterialTheme.typography.bodyLarge)

        if (isRegisterSuccess) {
            // Mostrar mensaje de confirmación después de un registro exitoso
            Text(text = "$name, tu registro fue correcto. Revisa tu email $email para confirmar tu registro.", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = successMessage, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoading = true
                    errorMessage = ""
                    successMessage = ""
                    scope.launch {
                        try {
                            val result = authRepository.resendVerificationEmail()
                            result.onSuccess { resendResponse ->
                                withContext(Dispatchers.Main) {
                                    successMessage = resendResponse.message
                                }
                            }.onFailure { e ->
                                withContext(Dispatchers.Main) {
                                    errorMessage = e.message ?: "Failed to resend verification email"
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                errorMessage = e.message ?: "Failed to resend verification email"
                            }
                        } finally {
                            withContext(Dispatchers.Main) {
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = canResendEmail && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    if (canResendEmail) {
                        Text("Resend Verification Email")
                    } else {
                        Text("Puedes reenviar el email en $timer segundos")
                    }
                }
            }
        } else {
            // Mostrar formulario de registro
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

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

            OutlinedTextField(
                value = passwordConfirmation,
                onValueChange = { passwordConfirmation = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoading = true
                    errorMessage = ""
                    successMessage = ""
                    scope.launch {
                        try {
                            val result = authRepository.register(name, email, password, passwordConfirmation)
                            result.onSuccess {
                                withContext(Dispatchers.Main) {
                                    successMessage = "Successfully registered. Please check your email for verification."
                                    isRegisterSuccess = true  // Actualiza el estado cuando el registro es exitoso
                                    timer = 60 // Reiniciar el temporizador
                                }
                            }.onFailure { e ->
                                withContext(Dispatchers.Main) {
                                    errorMessage = e.message ?: "Registration failed"
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                errorMessage = e.message ?: "Registration failed"
                            }
                        } finally {
                            withContext(Dispatchers.Main) {
                                isLoading = false
                            }
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
                    Text("Register")
                }
            }

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes una cuenta? Inicia sesión")
        }
    }
}
