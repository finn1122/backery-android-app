package com.example.mybakery.ui.components

import androidx.compose.runtime.Composable
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
                    // Navegar a una pantalla principal o pantalla de inicio
                    navController.navigate("home") {
                        // Esto borra la pila de navegación para que el usuario no pueda volver a la pantalla de inicio de sesión con el botón de retroceso
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onAdminBakeryCheck = {
                    // Navegar a una pantalla específica de administradores o a una pantalla de verificación
                    navController.navigate("adminBakery") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                authRepository = authRepository,
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        // Define aquí más composables para otras pantallas, como la pantalla principal o de administradores
        composable("home") {
            // Aquí deberías mostrar la pantalla principal de tu aplicación
        }
        composable("adminBakery") {
            // Aquí deberías mostrar la pantalla de administradores o de verificación de la panadería
        }
    }
}
