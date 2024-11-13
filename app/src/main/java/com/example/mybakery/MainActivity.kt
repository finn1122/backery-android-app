package com.example.mybakery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybakery.ui.bakery.BakerySetupScreen
import com.example.mybakery.ui.login.LoginScreen
import com.example.mybakery.ui.register.RegisterScreen
import com.example.mybakery.viewmodel.LoginViewModel
import com.example.mybakery.ui.theme.MyBakeryTheme
import com.example.mybakery.viewmodel.BakerySetupViewModel
import com.example.mybakery.viewmodel.RegisterViewModel
import com.example.mybakery.viewmodel.SetupBakeryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBakeryTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavGraph(navController)
                }
            }
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    val loginViewModel: LoginViewModel = viewModel()
    val registerViewModel: RegisterViewModel = viewModel()
    val bakeryViewModel: BakerySetupViewModel = viewModel()

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
        composable("bakery_setup") {
            BakerySetupScreen(viewModel = bakeryViewModel, navController = navController)
        }
    }
}
