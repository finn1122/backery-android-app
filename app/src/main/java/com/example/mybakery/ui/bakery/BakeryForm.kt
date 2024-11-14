package com.example.mybakery.ui.bakery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mybakery.R
import com.example.mybakery.viewmodel.BakerySetupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BakeryForm (
    viewModel: BakerySetupViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bakery Setup", fontSize = 20.sp) },
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
            Form(
                modifier = Modifier.align(Alignment.Center),
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun Form(modifier: Modifier, viewModel: BakerySetupViewModel, navController: NavController) {
    val name: String by viewModel.name.observeAsState(initial = "")
    val address: String by viewModel.address.observeAsState(initial = "")
    val openingHours: String by viewModel.openingHours.observeAsState(initial = "")
    val profilePictureBitmap: Bitmap? by viewModel.profilePictureBitmap.observeAsState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nombre de la panadería
            NameField(name) { viewModel.onBakeryChanged(it, address, openingHours, profilePictureBitmap) }

            // Dirección de la panadería
            AddressField(address) { viewModel.onBakeryChanged(name, it, openingHours, profilePictureBitmap) }

            // Horario de atención
            OpeningHoursField(openingHours) { viewModel.onBakeryChanged(name, address, it, profilePictureBitmap) }

            // Imagen de perfil
            ProfilePicture(
                profilePictureBitmap = profilePictureBitmap,
                onImageSelected = { viewModel.onBakeryChanged(name, address, openingHours, it) }
            )

            // Botón para guardar
            Button(
                onClick = { /* Acción de guardar */ },
                modifier = Modifier.padding(top = 16.dp),
                enabled = viewModel.saveBakeryEnable.observeAsState(initial = false).value
            ) {
                Text("Guardar")
            }
        }
    }
}

@Composable
fun NameField(name: String, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = name,
        onValueChange = { onTextFieldChanged(it) },
        label = { Text("Name") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AddressField(address: String, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = address,
        onValueChange = { onTextFieldChanged(it) },
        label = { Text("Dirección") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun OpeningHoursField(openingHours: String, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = openingHours,
        onValueChange = { onTextFieldChanged(it) },
        label = { Text("Horario de atención") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ProfilePicture(
    profilePictureBitmap: Bitmap?,
    onImageSelected: (Bitmap) -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream)
            if (selectedBitmap != null) {
                onImageSelected(selectedBitmap)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        profilePictureBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Imagen de perfil",
                modifier = Modifier.size(128.dp)
            )
        } ?: Image(
            bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo).asImageBitmap(),
            contentDescription = "Imagen predeterminada",
            modifier = Modifier.size(128.dp)
        )

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleccionar Imagen")
        }
    }
}
