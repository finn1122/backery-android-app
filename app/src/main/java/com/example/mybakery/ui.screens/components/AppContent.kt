package com.example.mybakery.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybakery.repository.AuthRepository
import com.example.mybakery.ui.screens.components.login.RegisterScreen
import com.example.mybakery.ui.screens.login.LoginScreen

@Composable
fun AppContent(authRepository: AuthRepository) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                authRepository = authRepository,
                onLoginSuccess = {
                    // Puedes navegar a una pantalla principal o página de inicio si tienes una
                    // navController.navigate("home")
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                authRepository = authRepository,
                onRegisterSuccess = {
                    // Volver a la pantalla de inicio de sesión después del registro
                    navController.popBackStack()
                }
            )
        }
    }
}
