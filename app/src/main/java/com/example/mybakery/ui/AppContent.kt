package com.example.mybakery.ui.components

/*@Composable
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
*/
