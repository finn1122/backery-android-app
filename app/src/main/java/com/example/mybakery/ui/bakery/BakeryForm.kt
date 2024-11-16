package com.example.mybakery.ui.bakery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.mybakery.data.model.bakery.Bakery
import com.example.mybakery.data.model.bakery.BakeryResponse
import com.example.mybakery.utils.PreferencesHelper
import com.example.mybakery.viewmodel.BakerySetupViewModel

@Composable
fun BakeryForm (
    viewModel: BakerySetupViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {

    // Manejo del botón físico de regresar
    BackHandler {
        onBack()
    }
    // Caja contenedora
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Botón de retroceso personalizado
        IconButton(
            onClick = { onBack() },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Retroceder")
        }

        Form(
            modifier = Modifier.align(Alignment.Center),
            viewModel = viewModel,
            navController = navController
        )
    }


}

@Composable
fun Form(
    modifier: Modifier,
    viewModel: BakerySetupViewModel,
    navController: NavController
) {
    val name: String by viewModel.name.observeAsState(initial = "")
    val address: String by viewModel.address.observeAsState(initial = "")
    val openingHours: String by viewModel.openingHours.observeAsState(initial = "")
    val profilePictureBitmap: Bitmap? by viewModel.profilePictureBitmap.observeAsState()
    val saveBakeryEnable: Boolean by viewModel.saveBakeryEnable.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val isBakeryCreated: Boolean by viewModel.isBakeryCreated.observeAsState(initial = false)
    val saveBakeryResult: Result<String>? by viewModel.saveBakeryResult.observeAsState()

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            if (isBakeryCreated) {
                Text(
                    "¡Panadería creada con éxito!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                )
            } else {
                val result = saveBakeryResult
                if (result != null && result.isFailure) {
                    val exceptionMessage = result.exceptionOrNull()?.message
                    Text(
                        "Error: $exceptionMessage",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                    )
                }
            }

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
                        onClick = {
                            viewModel.submitBakeryData()
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        enabled = saveBakeryEnable
                    ) {
                        Text("Guardar")
                    }
                }
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
