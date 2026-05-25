package com.hotelbooking.mobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotelbooking.mobile.domain.repository.AuthRepository
import com.hotelbooking.mobile.model.RegisterRequest
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var message by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    fun resetState() {
        firstName = ""
        lastName = ""
        email = ""
        password = ""
        phoneNumber = ""
        message = ""
    }

    fun register(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank()) {
            message = "Vă rugăm să completați toate câmpurile obligatorii"
            return
        }

        viewModelScope.launch {
            isLoading = true
            message = "Se înregistrează..."
            val result = repository.register(RegisterRequest(
                name = firstName,
                surname = lastName,
                email = email,
                password = password,
                phoneNumber = phoneNumber
            ))
            isLoading = false
            
            result.onSuccess {
                message = "✅ Înregistrare reușită! Acum vă puteți autentifica."
                onSuccess()
            }.onFailure {
                message = "❌ Eroare: ${it.message}"
            }
        }
    }
}
