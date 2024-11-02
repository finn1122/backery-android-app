package com.example.mybakery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mybakery.ui.theme.MyBakeryTheme
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import com.example.mybakery.repository.AuthRepository
import com.example.mybakery.ui.screens.LoginScreen
import com.example.mybakery.ui.theme.MyBakeryTheme
import com.example.mybakery.data.network.RetrofitClient


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyBakeryTheme {
                // Aquí se establece el contenido de la pantalla
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Mostrar la pantalla de inicio de sesión
                    LoginScreenContent(innerPadding)
                }
            }
        }
    }
}

@Composable
fun LoginScreenContent(innerPadding: PaddingValues) {
    // Crear una instancia de ApiService usando Retrofit
    val apiService = remember { RetrofitClient.apiService }
    val authRepository = remember { AuthRepository() }

    // Llama a LoginScreen y maneja el éxito del inicio de sesión
    LoginScreen(authRepository = authRepository) { loginResponse ->
        // Aquí puedes manejar el token y navegar a la pantalla principal
        // Por ejemplo, puedes navegar a otra pantalla o cambiar la UI
        println("Login successful! Token: ${loginResponse.token}")
        // Redirigir o cambiar a otra pantalla
    }
}