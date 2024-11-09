package com.example.mybakery.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.mybakery.repository.AuthRepository
import com.example.mybakery.ui.login.*
import com.example.mybakery.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(viewModel: RegisterViewModel, navController: NavController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Register(Modifier.align(Alignment.Center), viewModel)
    }

}

@Composable
fun Register(modifier: Modifier, viewModel: RegisterViewModel) {
    val name : String by viewModel.name.observeAsState(initial = "")
    val email : String by viewModel.email.observeAsState(initial = "")
    val password : String by viewModel.password.observeAsState(initial = "")
    val passwordConfirmation : String by viewModel.passwordConfirmation.observeAsState(initial = "")
    val registerEnable : Boolean by viewModel.registerEnable.observeAsState(initial = false)
    val isLoading : Boolean by viewModel.isLoading.observeAsState(initial = false)
    val registerResult : Result<String>? by viewModel.registerResult.observeAsState()
    val canSendEmail : Boolean by viewModel.canSendEmail.observeAsState(initial = false)
    //var timer by remember { mutableStateOf(60) }
    val coroutineScope = rememberCoroutineScope()

    if(isLoading){
        Box(Modifier.fillMaxSize()){
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }else{
        Column (
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            nameField(name, {viewModel.onRegisterChanged(it, email, password, passwordConfirmation)})
            Spacer(modifier = Modifier.height(4.dp))
            emailField(email, {viewModel.onRegisterChanged(name, it, password, passwordConfirmation)})
            Spacer(modifier = Modifier.height(4.dp))
            passwordField(password, {viewModel.onRegisterChanged(name, email, it, passwordConfirmation)})
            Spacer(modifier = Modifier.height(8.dp))
            passwordConfirmationField(passwordConfirmation, {viewModel.onRegisterChanged(name, email, password, it)})
            Spacer(modifier = Modifier.height(8.dp))
            registerButton(modifier = Modifier.align(Alignment.CenterHorizontally), registerEnable){
                coroutineScope.launch {
                    viewModel.onRegisterSelected()
                }
            }
        }
    }
}

@Composable
fun nameField(name: String, onTextFiledChange: (String) -> Unit) {
    OutlinedTextField(
        value = name,
        onValueChange = {onTextFiledChange(it) },
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun emailField(email: String, onTextFiledChange: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = {onTextFiledChange(it) },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth()

    )
}
@Composable
fun passwordField(password: String, onTextFiledChange: (String) -> Unit) {
    OutlinedTextField(
        value = password,
        onValueChange = { onTextFiledChange(it) },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}
@Composable
fun passwordConfirmationField(passwordConfirmation: String, onTextFiledChange: (String) -> Unit) {
    OutlinedTextField(
        value = passwordConfirmation,
        onValueChange = { onTextFiledChange(it) },
        label = { Text("Confirm Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}
@Composable
fun registerButton(modifier: Modifier = Modifier, registerEnable: Boolean, onRegisterSelected: () -> Unit) {
    Button(
        onClick = { onRegisterSelected() },
        enabled = registerEnable,
        modifier = modifier,
        ) {
        Text("Register")
    }
}

/*@Composable
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

    LaunchedEffect(isRegisterSuccess, timer) {
        if (isRegisterSuccess) {
            canResendEmail = false
            while (timer > 0) {
                delay(1000L)
                timer--
            }
            canResendEmail = true
        }
    }

    // Correct timer reset and button reactivation on resend verification email
    LaunchedEffect(timer) {
        if (timer == 0) {
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
            Text(text = "$name, tu registro fue correcto. Revisa tu email $email para confirmar tu registro.", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

            if (successMessage.isNotEmpty()) {
                Text(text = successMessage, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    isLoading = true
                    errorMessage = ""
                    successMessage = ""
                    scope.launch {
                        try {
                            val result = authRepository.resendVerificationEmail(email)
                            result.onSuccess { resendResponse ->
                                successMessage = resendResponse.message
                                timer = 60 // Reiniciar el temporizador después del reenvío
                                canResendEmail = false
                            }.onFailure { e ->
                                errorMessage = e.message ?: "Failed to resend verification email"
                                canResendEmail = true // Habilitar el botón en caso de fallo
                            }
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Failed to resend verification email"
                            canResendEmail = true // Habilitar el botón en caso de fallo
                        } finally {
                            isLoading = false
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

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
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
                                successMessage = "Successfully registered. Please check your email for verification."
                                isRegisterSuccess = true  // Actualiza el estado cuando el registro es exitoso
                                timer = 60 // Reiniciar el temporizador
                            }.onFailure { e ->
                                errorMessage = e.message ?: "Registration failed"
                            }
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Registration failed"
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
*/
