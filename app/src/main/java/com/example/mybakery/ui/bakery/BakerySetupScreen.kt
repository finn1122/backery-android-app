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
import com.example.mybakery.ui.register.Register

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
