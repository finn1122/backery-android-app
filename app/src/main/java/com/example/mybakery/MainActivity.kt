package com.example.mybakery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mybakery.ui.theme.MyBakeryTheme
import com.example.mybakery.ui.components.AppContent // Asegúrate de mantener este import
import com.example.mybakery.repository.AuthRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Instanciar AuthRepository
        val authRepository = AuthRepository()

        setContent {
            MyBakeryTheme {
                AppContent(authRepository = authRepository)
            }
        }
    }
}
