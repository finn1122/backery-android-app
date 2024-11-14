package com.example.mybakery.ui.bakery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mybakery.viewmodel.BakerySetupViewModel
import androidx.compose.runtime.*

@Composable
fun BakerySetupScreen(viewModel: BakerySetupViewModel, navController: NavController) {

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        BakeryForm(
            viewModel,
            navController,
            Modifier.align(Alignment.Center)
        )
    }


}
