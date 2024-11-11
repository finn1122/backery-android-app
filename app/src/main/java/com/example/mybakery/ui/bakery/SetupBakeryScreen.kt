package com.example.mybakery.ui.bakery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.mybakery.viewmodel.SetupBakeryViewModel

@Composable
fun SetupBakeryScreen(viewModel: SetupBakeryViewModel, navController: NavController) {
    /*Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Configura tu nueva Panadería", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Completa la configuración para empezar a gestionar tu panadería.", style = MaterialTheme.typography.bodyMedium)
    }*/
    BakeryForm(viewModel = viewModel, modifier = Modifier)


}
