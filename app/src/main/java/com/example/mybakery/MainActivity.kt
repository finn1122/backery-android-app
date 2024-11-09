package com.example.mybakery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mybakery.ui.login.LoginScreen
import com.example.mybakery.viewmodel.LoginViewModel
import com.example.mybakery.ui.theme.MyBakeryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val loginViewModel = LoginViewModel()

        // Instanciar PreferencesHelper
        //val preferencesHelper = PreferencesHelper(this)

        // Instanciar AuthRepository
        //val authRepository = AuthRepository(preferencesHelper)

        setContent {
            MyBakeryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    LoginScreen(loginViewModel)
                }
            }
        }
    }

    private fun enableEdgeToEdge() {
        // Configuración para activar características de diseño en pantalla completa.
    }
}
