package com.example.mybakery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybakery.ui.login.LoginScreen
import com.example.mybakery.ui.register.RegisterScreen
import com.example.mybakery.viewmodel.LoginViewModel
import com.example.mybakery.ui.theme.MyBakeryTheme
import com.example.mybakery.viewmodel.RegisterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val loginViewModel = LoginViewModel()
        val registerViewModel = RegisterViewModel()

        // Instanciar PreferencesHelper
        //val preferencesHelper = PreferencesHelper(this)

        // Instanciar AuthRepository
        //val authRepository = AuthRepository(preferencesHelper)

        setContent {
            MyBakeryTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavGraph(navController, loginViewModel, registerViewModel)
                }
            }
        }
    }

    private fun enableEdgeToEdge() {
        // Configuración para activar características de diseño en pantalla completa.
    }
}

@Composable
fun SetupNavGraph(
    navController: androidx.navigation.NavHostController,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(viewModel = loginViewModel, navController = navController)
        }
        composable("register") {
            RegisterScreen(viewModel = registerViewModel, navController = navController)
        }
    }
}
