package com.example.mybakery.ui.bakery

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mybakery.R
import com.example.mybakery.data.model.bakery.BakeryResponse
import com.example.mybakery.ui.login.*
import com.example.mybakery.ui.register.*
import com.example.mybakery.viewmodel.BakerySetupViewModel
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mybakery.viewmodel.SetupBakeryViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BakeryForm (
    viewModel: BakerySetupViewModel,
    navController: NavController,
    modifier: Modifier,
    ){

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bakery Setup") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues -> // Usa paddingValues en vez de it
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues) // Aplica el padding proporcionado aquí
                .padding(16.dp)
        ) {
            Form(Modifier.align(Alignment.Center), viewModel, navController)
        }
    }

}

@Composable
fun Form(modifier: Modifier, viewModel: BakerySetupViewModel, navController: NavController) {
    val name : String by viewModel.name.observeAsState(initial = "")
    val address : String by viewModel.address.observeAsState(initial = "")
    val openingHours : String by viewModel.openingHours.observeAsState(initial = "")
    val profilePictureBitmap: Bitmap? by viewModel.profilePictureBitmap.observeAsState()


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
                NameField(name) { viewModel.onBakeryChanged(it, address, openingHours, profilePictureBitmap) }
                Spacer(modifier = Modifier.height(8.dp))
                AddressField(address) { viewModel.onBakeryChanged(name, it, openingHours, profilePictureBitmap) }
                Spacer(modifier = Modifier.height(16.dp))
                OpeningHoursField(openingHours) { viewModel.onBakeryChanged(name, address, it, profilePictureBitmap) }
                Spacer(modifier = Modifier.height(16.dp))
                ProfilePicture(
                    profilePictureBitmap = profilePictureBitmap,
                    onImageSelected = { viewModel.onBakeryChanged(name, address, openingHours, it) }
                )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}
@Composable
fun AddressField(address: String, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = address,
        onValueChange = { onTextFieldChanged(it) },
        label = { Text("Dirección") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}
@Composable
fun OpeningHoursField(openingHours: String, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = openingHours,
        onValueChange = { onTextFieldChanged(it) },
        label = { Text("Horario de atención") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar la imagen proporcionada por el ViewModel
        profilePictureBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleccionar Imagen")
        }
    }
}
