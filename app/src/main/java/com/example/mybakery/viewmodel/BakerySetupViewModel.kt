package com.example.mybakery.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybakery.data.network.RetrofitClient

class BakerySetupViewModel : ViewModel() {

    private val apiService = RetrofitClient.apiService

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    private val _openingHours = MutableLiveData<String>()
    val openingHours: LiveData<String> = _openingHours

    private val _saveBakeryEnable = MutableLiveData<Boolean>()
    val saveBakeryEnable: LiveData<Boolean> = _saveBakeryEnable

    // New functions to update the LiveData values
    fun updateName(newName: String) {
        _name.value = newName
        validateForm()
    }

    fun updateAddress(newAddress: String) {
        _address.value = newAddress
        validateForm()
    }

    fun updateOpeningHours(newOpeningHours: String) {
        _openingHours.value = newOpeningHours
        validateForm()
    }

    private fun validateForm() {
        _saveBakeryEnable.value = isValidName(_name.value ?: "") &&
            isValidAddress(_address.value ?: "") &&
            isValidOpeningHours(_openingHours.value ?: "")
    }

    private fun isValidName(name: String): Boolean = name.isNotBlank()
    private fun isValidAddress(address: String): Boolean = address.isNotBlank()
    private fun isValidOpeningHours(openingHours: String): Boolean = openingHours.isNotBlank()

    fun onBakeryChanged(name: String, address: String, openingHours: String) {
        _name.value = name
        _address.value = address
        _openingHours.value = openingHours
        _saveBakeryEnable.value = isValidName(name)
            && isValidAddress(address)
            && isValidOpeningHours(openingHours)
    }


}