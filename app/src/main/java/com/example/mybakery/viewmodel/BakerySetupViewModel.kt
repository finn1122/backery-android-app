package com.example.mybakery.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Patterns
import androidx.lifecycle.*
import com.example.mybakery.R
import com.example.mybakery.data.model.bakery.Bakery
import com.example.mybakery.data.model.bakery.BakeryRequest
import com.example.mybakery.data.model.bakery.BakeryResponse
import com.example.mybakery.data.network.RetrofitClient
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class BakerySetupViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.apiService

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    private val _openingHours = MutableLiveData<String>()
    val openingHours: LiveData<String> = _openingHours

    private val _profilePictureUrl = MutableLiveData<String?>()
    val profilePictureUrl: LiveData<String?> = _profilePictureUrl

    private val _profilePictureBitmap = MutableLiveData<Bitmap?>()
    val profilePictureBitmap: LiveData<Bitmap?> = _profilePictureBitmap

    private val _saveBakeryEnable = MutableLiveData<Boolean>()
    val saveBakeryEnable: LiveData<Boolean> = _saveBakeryEnable

    private val _active = MutableLiveData<Boolean>()
    val active: LiveData<Boolean> = _active

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        // Cargar la imagen predeterminada desde el archivo drawable/logo.png
        val defaultBitmap = BitmapFactory.decodeResource(application.resources, R.drawable.logo)
        _profilePictureBitmap.value = defaultBitmap
    }

    fun isValidProfilePicture(profilePictureBitmap: Bitmap?): Boolean {
        return profilePictureBitmap != null
    }

    fun isValidActive(active: Boolean): Boolean = active


    private fun isValidName(name: String): Boolean = name.isNotBlank()
    private fun isValidAddress(address: String): Boolean = address.isNotBlank()
    private fun isValidOpeningHours(openingHours: String): Boolean = openingHours.isNotBlank()

    fun onBakeryChanged(
        name: String,
        address: String,
        openingHours: String,
        profilePictureBitmap: Bitmap?
    ) {
        _name.value = name
        _address.value = address
        _openingHours.value = openingHours
        _profilePictureBitmap.value = profilePictureBitmap
        _saveBakeryEnable.value = isValidName(name)
            && isValidAddress(address)
            && isValidOpeningHours(openingHours)
            && isValidProfilePicture(profilePictureBitmap)
    }

    fun submitBakeryData(userId: Int, token: String, bakeryData: Bakery) {
        viewModelScope.launch {
            _isLoading.value = true

            val request = bakeryDataToRequest(bakeryData)

            val response = apiService.createBakery(
                token = token,
                userId = userId,
                request = request
            )

            if (response.isSuccessful) {
                val updatedBakeryData = mapResponseToData(response.body()!!)
                _name.value = updatedBakeryData.name
                _address.value = updatedBakeryData.address
                _openingHours.value = updatedBakeryData.openingHours
                _profilePictureUrl.value = updatedBakeryData.profilePictureUrl
                _profilePictureBitmap.value = null  // Limpiar para evitar confusión
                _active.value = updatedBakeryData.active
                // Manejar el éxito
            } else {
                // Manejar el error
            }

            _isLoading.value = false
        }
    }

    fun updateBakeryData(userId: Int, bakeryId: Int, token: String, bakeryData: Bakery) {
        viewModelScope.launch {
            _isLoading.value = true

            val request = bakeryDataToRequest(bakeryData)

            val response = apiService.updateBakery(
                token = token,
                userId = userId,
                bakeryId = bakeryId,
                request = request
            )

            if (response.isSuccessful) {
                val updatedBakeryData = mapResponseToData(response.body()!!)
                _name.value = updatedBakeryData.name
                _address.value = updatedBakeryData.address
                _openingHours.value = updatedBakeryData.openingHours
                _profilePictureUrl.value = updatedBakeryData.profilePictureUrl
                _profilePictureBitmap.value = null  // Limpiar para evitar confusión
                _active.value = updatedBakeryData.active
                // Manejar el éxito
            } else {
                // Manejar el error
            }

            _isLoading.value = false
        }
    }

    private fun mapResponseToData(response: BakeryResponse): Bakery {
        return Bakery(
            name = response.name,
            address = response.address,
            openingHours = response.openingHours,
            profilePictureUrl = response.profilePicture,
            active = response.active,
            id = response.id
        )
    }

    private fun bakeryDataToRequest(data: Bakery): BakeryRequest {
        val base64Image = data.profilePictureBitmap?.let {
            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        return BakeryRequest(
            name = data.name,
            address = data.address,
            openingHours = data.openingHours,
            profilePicture = base64Image,
            active = data.active
        )
    }


}
