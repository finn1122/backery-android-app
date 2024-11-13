package com.example.mybakery.ui.bakery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mybakery.ui.login.Login
import com.example.mybakery.viewmodel.BakerySetupViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BakerySetupScreen(viewModel: BakerySetupViewModel, navController: NavController) {

    BakeryForm(
        navController = navController,
        viewModel = viewModel,
        modifier = Modifier.fillMaxSize()
    )
}
