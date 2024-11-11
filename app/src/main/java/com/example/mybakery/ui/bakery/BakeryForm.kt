package com.example.mybakery.ui.bakery

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mybakery.viewmodel.SetupBakeryViewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mybakery.ui.register.*
import kotlinx.coroutines.launch

@Composable
fun BakeryForm (viewModel: SetupBakeryViewModel, modifier: Modifier){

    val name : String by viewModel.name.observeAsState(initial = "")
    val address : String by viewModel.address.observeAsState(initial = "")
    val openingHours : String by viewModel.openingHours.observeAsState(initial = "")
    val profilePicture : String by viewModel.profilePicture.observeAsState(initial = "")
    val active : Boolean by viewModel.active.observeAsState(initial = false)
    val isLoading : Boolean by viewModel.isLoading.observeAsState(initial = false)

    val coroutineScope = rememberCoroutineScope()

    if(isLoading){
        Box(Modifier.fillMaxSize()){
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }else{
        Text(text = "Setup Bakery")
        Spacer(modifier = Modifier.height(8.dp))
        nameField(name, { viewModel.onBakeryChanged(it, email, password, passwordConfirmation) })

    }
    // Mostrar mensajes basados en el resultado del login
    saveBakeryResult?.let {
        if (it.isFailure) {
            Text("Register failed: ${it.exceptionOrNull()?.message}", color = Color.Red)
        }
    }


}

fun NameField(){

}
