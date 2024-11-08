package com.example.mybakery.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybakery.repository.AuthRepository
import com.example.mybakery.ui.screens.login.LoginScreen
import com.example.mybakery.ui.screens.bakery.BakeryDashboardScreen
import com.example.mybakery.ui.screens.bakery.SetupBakeryScreen
import com.example.mybakery.ui.screens.login.RegisterScreen
import com.example.mybakery.utils.PreferencesHelper

@Composable
fun AppContent(authRepository: AuthRepository) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val preferencesHelper = PreferencesHelper(context)

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                authRepository = authRepository,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onAdminBakeryCheck = { hasBakery ->
                    if (hasBakery) {
                        navController.navigate("bakeryDashboard") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    } else {
                        navController.navigate("setupBakery") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
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
        composable("home") {
            // Aquí deberías mostrar la pantalla principal de tu aplicación
        }
        composable("bakeryDashboard") {
            BakeryDashboardScreen()
        }
        composable("setupBakery") {
            SetupBakeryScreen()
        }
    }
}
