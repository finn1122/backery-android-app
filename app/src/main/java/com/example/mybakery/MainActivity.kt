package com.example.mybakery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mybakery.ui.theme.MyBakeryTheme
import androidx.compose.foundation.layout.PaddingValues
import com.example.mybakery.repository.AuthRepository
import com.example.mybakery.ui.screens.components.login.LoginScreen
import com.example.mybakery.ui.screens.components.login.RegisterScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybakery.ui.navigation.Screen
import com.example.mybakery.ui.theme.MyBakeryTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyBakeryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyBakeryApp(authRepository = AuthRepository(), innerPadding = innerPadding)
                }
            }
        }
    }
}

@Composable
fun MyBakeryApp(authRepository: AuthRepository, innerPadding: PaddingValues) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.name, modifier = Modifier.padding(innerPadding)) {
        composable(Screen.Login.name) {
            LoginScreen(
                authRepository = authRepository,
                onLoginSuccess = { loginResponse ->
                    // Manejo del éxito del login
                    println("Login successful! Token: ${loginResponse.token}")
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.name)
                }
            )
        }
        composable(Screen.Register.name) {
            RegisterScreen(
                authRepository = authRepository,
                onRegisterSuccess = {
                    navController.popBackStack()
                }
            )
        }
    }
}
