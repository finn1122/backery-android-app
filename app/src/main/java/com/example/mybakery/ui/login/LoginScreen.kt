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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mybakery.R
import com.example.mybakery.viewmodel.LoginViewModel
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Login(Modifier.align(Alignment.Center), viewModel, navController)
    }

    /*var email by remember { mutableStateOf("") }
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
    }*/
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, navController: NavController) {

    val email : String by viewModel.email.observeAsState(initial = "")
    val password : String by viewModel.password.observeAsState(initial = "")
    val loginEnable : Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val isLoading : Boolean by viewModel.isLoading.observeAsState(initial = false)
    val loginResult : Result<String>? by viewModel.loginResult.observeAsState()
    val coroutineScope = rememberCoroutineScope()

    if(isLoading){
        Box(Modifier.fillMaxSize()){
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }else{
        Column (modifier = modifier){
            HeaderImage(Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))
            EmailField(email, {viewModel.onLoginChanged(it, password)})
            Spacer(modifier = Modifier.height(4.dp))
            PasswordField(password, {viewModel.onLoginChanged(email, it)})
            Spacer(modifier = Modifier.height(8.dp))
            ForgotPassword(Modifier.align(Alignment.End))
            Spacer(modifier = Modifier.height(16.dp))
            LoginButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                loginEnable
            ) {
                coroutineScope.launch {
                    viewModel.onLoginSelected()
                }
            }
            NotRegisterYet(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                navController.navigate("register")
            }
        }
    }
    // Mostrar mensajes basados en el resultado del login
    loginResult?.let {
        if (it.isSuccess) {
            Text("Login successful: ${it.getOrNull()}", color = Color.Green)
        } else if (it.isFailure) {
            Text("Login failed: ${it.exceptionOrNull()?.message}", color = Color.Red)
        }
    }


}

@Composable
fun LoginButton(modifier: Modifier = Modifier, loginEnable : Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        enabled = loginEnable,
        modifier = modifier
    ) {
            Text("Login")
    }
}

@Composable
fun PasswordField(password : String, onTexFieldChanged: (String) -> Unit) {
    TextField(
        value = password,
        onValueChange = { onTexFieldChanged(it) },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.default_logo),
        contentDescription = "Header",
    )
}

@Composable
fun EmailField(email: String, onTextFiledChange: (String) -> Unit) {

    TextField(
        value = email,
        onValueChange = { onTextFiledChange(it) },
        placeholder = { Text("Email") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "¿Olvidaste la contraseña?",
        modifier = Modifier.clickable {  },
        fontSize = 12.sp, fontWeight = FontWeight.Bold,
        color = Color(0xFFFB9600)
    )
}

@Composable
fun NotRegisterYet(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = modifier
    ) {
        Text("¿Aún no estás registrado?")
    }
}
