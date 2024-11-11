package com.example.mybakery.data.model.bakery
import android.graphics.Bitmap

data class Bakery (
    val name: String,
    val address: String,
    val openingHours: String,
    val profilePictureUrl: String? = null,  // Obtención de la API
    val profilePictureBitmap: Bitmap? = null,  // Archivo local para subir
    val active: Boolean,
    val id: Int = -1
)
