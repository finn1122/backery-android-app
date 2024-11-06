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
import com.example.mybakery.data.model.BakeryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.mybakery.data.model.LoginResponse
import com.example.mybakery.repository.AuthRepository
import com.example.mybakery.utils.PreferencesHelper
import retrofit2.Response

@Composable
fun LoginScreen(
    authRepository: AuthRepository,
    onLoginSuccess: () -> Unit,
    onAdminBakeryCheck: (Boolean) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val preferencesHelper = PreferencesHelper(context)

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
                scope.launch {
                    try {
                        val loginResult = withContext(Dispatchers.IO) {
                            authRepository.login(email, password)
                        }
                        loginResult.onSuccess { loginResponse ->
                            val userRole = loginResponse.user.roles.firstOrNull()
                            val userId = loginResponse.user.id
                            val token = loginResponse.token

                            // Guardar datos en las preferencias
                            Log.d("LoginScreen", "Login successful. Saving token and user role.")
                            preferencesHelper.saveToken(token)
                            preferencesHelper.saveUserRole(userRole ?: "")
                            preferencesHelper.saveUserName(loginResponse.user.name)
                            preferencesHelper.saveUserEmail(loginResponse.user.email)

                            if (userRole != null) {
                                Toast.makeText(context, "Rol del usuario: $userRole", Toast.LENGTH_SHORT).show()
                            }

                            Log.d("LoginScreen", "User role: $userRole")

                            if (userRole == "admin" && token.isNotEmpty()) {
                                Log.d("LoginScreen", "Logging in as admin. Verifying bakery...")

                                val bakeryResponse: Response<List<BakeryResponse>> = withContext(Dispatchers.IO) {
                                    authRepository.verifyBakery("Bearer $token", userId)
                                }

                                Log.d("LoginScreen", "Bakery Response Code: ${bakeryResponse.code()}")

                                if (bakeryResponse.isSuccessful) {
                                    val bakeryList = bakeryResponse.body()
                                    bakeryList?.let {
                                        val hasBakery = it.isNotEmpty()
                                        Log.d("LoginScreen", "Bakery List: $bakeryList")
                                        onAdminBakeryCheck(hasBakery)
                                    }
                                } else {
                                    errorMessage = "Error al verificar la panadería: ${bakeryResponse.errorBody()?.string()}"
                                    Log.e("LoginScreen", "Error en la verificación de la panadería: ${bakeryResponse.errorBody()?.string()}")
                                }
                            } else {
                                onLoginSuccess()
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
