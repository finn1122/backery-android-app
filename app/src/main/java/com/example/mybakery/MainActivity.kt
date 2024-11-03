package com.example.mybakery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mybakery.repository.AuthRepository
import com.example.mybakery.ui.components.AppContent
import com.example.mybakery.ui.theme.MyBakeryTheme
import com.example.mybakery.utils.PreferencesHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Instanciar PreferencesHelper
        val preferencesHelper = PreferencesHelper(this)

        // Instanciar AuthRepository
        val authRepository = AuthRepository(preferencesHelper)

        setContent {
            MyBakeryTheme {
                AppContent(authRepository = authRepository)
            }
        }
    }

    private fun enableEdgeToEdge() {
        // Configuración para activar características de diseño en pantalla completa.
    }
}
