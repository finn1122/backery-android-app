package com.example.mybakery.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
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
    val isRegisterSuccess : Boolean by viewModel.isRegisterSuccess.observeAsState(initial = false)
    val canSendEmail : Boolean by viewModel.canSendEmail.observeAsState(initial = false)
    val isResendEmailSuccess : Boolean by viewModel.isResendEmailSuccess.observeAsState(initial = false)
    val resendEmailResult : Result<String>? by viewModel.resendEmailResult.observeAsState()
    val timerValue : Int? by viewModel.timerValue.observeAsState()

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
            if (isRegisterSuccess) {
                ShowRegisterSuccessMessage(name, email)
                Spacer(modifier = Modifier.height(16.dp))
                ShowResendEmailMessage(timerValue, canSendEmail, viewModel)
                resendEmailResult?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    if (it.isSuccess) {
                        Text(
                            text = "Correo reenviado correctamente: ${it.getOrNull()}",
                            color = Color.Green,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    } else if (it.isFailure) {
                        Text(
                            text = "Fallo al reenviar correo: ${it.exceptionOrNull()?.message}",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                nameField(name, { viewModel.onRegisterChanged(it, email, password, passwordConfirmation) })
                Spacer(modifier = Modifier.height(4.dp))
                emailField(email, { viewModel.onRegisterChanged(name, it, password, passwordConfirmation) })
                Spacer(modifier = Modifier.height(4.dp))
                passwordField(password, { viewModel.onRegisterChanged(name, email, it, passwordConfirmation) })
                Spacer(modifier = Modifier.height(8.dp))
                passwordConfirmationField(
                    passwordConfirmation,
                    { viewModel.onRegisterChanged(name, email, password, it) })
                Spacer(modifier = Modifier.height(8.dp))
                registerButton(modifier = Modifier.align(Alignment.CenterHorizontally), registerEnable) {
                    coroutineScope.launch {
                        viewModel.onRegisterSelected()
                    }
                }
            }
        }
    }
    // Mostrar mensajes basados en el resultado del login
    registerResult?.let {
        if (it.isFailure) {
            Text("Register failed: ${it.exceptionOrNull()?.message}", color = Color.Red)
        }
    }
}

@Composable
fun nameField(name: String, onTextFieldChange: (String) -> Unit) {
    OutlinedTextField(
        value = name,
        onValueChange = onTextFieldChange,
        label = { Text("Name") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}


@Composable
fun emailField(email: String, onTextFieldChange: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = onTextFieldChange,
        label = { Text("Email") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}


@Composable
fun passwordField(password: String, onTextFieldChange: (String) -> Unit) {
    OutlinedTextField(
        value = password,
        onValueChange = onTextFieldChange,
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}


@Composable
fun passwordConfirmationField(passwordConfirmation: String, onTextFieldChange: (String) -> Unit) {
    OutlinedTextField(
        value = passwordConfirmation,
        onValueChange = onTextFieldChange,
        label = { Text("Confirm Password") },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
fun registerButton(modifier: Modifier, registerEnable: Boolean, onRegisterSelected: () -> Unit) {
    Button(
        onClick = onRegisterSelected,
        enabled = registerEnable,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text("Register")
    }
}

@Composable
fun ShowRegisterSuccessMessage(name: String, email: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = "¡Registro exitoso, $name!",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Por favor, revisa tu correo electrónico ($email) para verificar tu cuenta.",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ShowResendEmailMessage(timerValue: Int?, canSendEmail: Boolean, viewModel: RegisterViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = "¿No recibiste el correo?.",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (timerValue != null && !canSendEmail) {
            Text(
                text = "Puedes reenviar el correo en: ${timerValue} segundos",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 4.dp),
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = { viewModel.resendVerificationEmail() },
            enabled = canSendEmail,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Text(text = "Reenviar correo de verificación")
        }
    }
}
