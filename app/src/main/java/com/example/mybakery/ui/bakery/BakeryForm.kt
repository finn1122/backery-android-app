package com.example.mybakery.ui.bakery

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mybakery.ui.login.*
import com.example.mybakery.ui.register.*
import com.example.mybakery.viewmodel.BakerySetupViewModel
import kotlinx.coroutines.launch
import okhttp3.Address


@Composable
fun BakeryForm (
    modifier: Modifier,
    navController: NavController,
    viewModel: BakerySetupViewModel
    ){

    val name : String by viewModel.name.observeAsState(initial = "")
    val address : String by viewModel.address.observeAsState(initial = "")
    val openingHours : String by viewModel.openingHours.observeAsState(initial = "")

    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NameField(name) { viewModel.onBakeryChanged(it, address, openingHours) }
            Spacer(modifier = Modifier.height(8.dp))
            AddressField(address) { viewModel.onBakeryChanged(name, it, openingHours) }
            Spacer(modifier = Modifier.height(16.dp))
            OpeningHoursField(openingHours) { viewModel.onBakeryChanged(name, address, it) }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }

}

@Composable
fun NameField(name: String, onTextFieldChange: (String) -> Unit)
{
        OutlinedTextField(
            value = name,
            onValueChange = { onTextFieldChange(it) },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
}

@Composable
fun AddressField(address: String, onTextFieldChange: (String) -> Unit) {
    OutlinedTextField(
        value = address,
        onValueChange = { onTextFieldChange(it) },
        label = { Text("Dirección") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
fun OpeningHoursField(openingHours: String, onTextFieldChange: (String) -> Unit) {
    OutlinedTextField(
        value = openingHours,
        onValueChange = { onTextFieldChange(it) },
        label = { Text("Horario de atención") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}
