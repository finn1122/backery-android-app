package com.example.mybakery.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mybakery.R
import com.example.mybakery.data.model.bakery.BakeryResponse
import com.example.mybakery.ui.bakery.BakerySetupScreen
import com.example.mybakery.viewmodel.BakerySetupViewModel
import com.example.mybakery.viewmodel.LoginViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Iniciar Sesión") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues -> // Usa paddingValues en vez de it
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues) // Aplica el padding proporcionado aquí
                .padding(16.dp)
        ) {
            Login(Modifier.align(Alignment.Center), viewModel, navController)
        }
    }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, navController: NavController) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val loginResult : Result<String>? by viewModel.loginResult.observeAsState()
    val isLoginSuccess : Boolean by viewModel.isLoginSuccess.observeAsState(initial = false)
    val bakeryResult: Result<List<BakeryResponse>?>? by viewModel.bakeryResult.observeAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderImage(Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(16.dp))
                EmailField(email) { viewModel.onLoginChanged(it, password) }
                Spacer(modifier = Modifier.height(8.dp))
                PasswordField(password) { viewModel.onLoginChanged(email, it) }
                Spacer(modifier = Modifier.height(16.dp))
                ForgotPassword(Modifier.align(Alignment.End))
                Spacer(modifier = Modifier.height(16.dp))
                LoginButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    loginEnable = loginEnable
                ) {
                    coroutineScope.launch { viewModel.onLoginSelected(context) }
                }
                Spacer(modifier = Modifier.height(16.dp))
                NotRegisterYet(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    navController.navigate("register")
                }

                LaunchedEffect(isLoginSuccess) {
                    if (isLoginSuccess) {
                        navController.navigate("bakery_setup")
                        viewModel.resetLoginSuccess() // Restablecer después de la navegación
                    }
                }

                // Mostrar mensajes basados en el resultado del login
                loginResult?.let {
                    if (it.isFailure) {
                        Text("Login failed: ${it.exceptionOrNull()?.message}", color = Color.Red)
                    }
                }
            }
        }
    }
}


@Composable
fun LoginButton(modifier: Modifier = Modifier, loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        enabled = loginEnable,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text("Login")
    }
}

@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = password,
        onValueChange = { onTextFieldChanged(it) },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.default_logo),
        contentDescription = "Header",
        modifier = modifier.padding(vertical = 16.dp).fillMaxWidth()
    )
}
@Composable
fun EmailField(email: String, onTextFieldChange: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = { onTextFieldChange(it) },
        label = { Text("Email") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "¿Olvidaste la contraseña?",
        modifier = modifier.clickable { },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.End
    )
}

@Composable
fun NotRegisterYet(modifier: Modifier, onClick: () -> Unit) {
    TextButton(onClick = { onClick() }, modifier = modifier) {
        Text(text = "¿Aún no estás registrado?", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
    }
}
