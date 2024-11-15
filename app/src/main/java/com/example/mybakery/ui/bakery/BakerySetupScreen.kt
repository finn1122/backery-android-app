package com.example.mybakery.ui.bakery

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mybakery.viewmodel.BakerySetupViewModel
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BakerySetupScreen(viewModel: BakerySetupViewModel, navController: NavController) {
    var showForm by remember { mutableStateOf(false) }
    var exitWarningShown by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    // Manejo del botón físico de regreso
    BackHandler {
        if (showForm) {
            showForm = false
        } else {
            if (exitWarningShown) {
                (context as? Activity)?.finish()
            } else {
                exitWarningShown = true
                coroutineScope.launch {
                    snackBarHostState.showSnackbar("Presione nuevamente para salir")
                    delay(2000) // Pide confirmación dentro de 2 segundos
                    exitWarningShown = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (showForm) "Configuración de Panadería" else "Bienvenida", fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (showForm) {
                BakeryForm(
                    viewModel,
                    navController,
                    Modifier.align(Alignment.Center),
                    onBack = { showForm = false }
                )
            } else {
                WelcomeScreen(
                    viewModel,
                    navController,
                    Modifier.align(Alignment.Center),
                    onFormNeeded = { showForm = true }
                )
            }
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun WelcomeScreen(viewModel: BakerySetupViewModel, navController: NavController, modifier: Modifier = Modifier, onFormNeeded: () -> Unit) {
    var isBakeryCreated by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Bienvenido!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Al parecer aún no tienes configurada tu panadería. Deberás completar estos pasos para poder continuar:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Pasos
        StepButton(
            step = "1. Crear Panadería",
            modifier = Modifier.fillMaxWidth(),
            onClick = onFormNeeded,
            isEnabled = true
        )
        StepButton(
            step = "2. Agregar Sucursal",
            modifier = Modifier.fillMaxWidth(),
            onClick = { /* Lógica para agregar sucursal */ },
            isEnabled = isBakeryCreated
        )
        StepButton(
            step = "3. Agregar Productos",
            modifier = Modifier.fillMaxWidth(),
            onClick = { /* Lógica para agregar productos */ },
            isEnabled = isBakeryCreated // Cambiar esto según tu lógica
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun StepButton(step: String, modifier: Modifier = Modifier, onClick: () -> Unit, isEnabled: Boolean) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Text(step)
    }
}
