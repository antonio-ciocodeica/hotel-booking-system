package com.hotelbooking.mobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotelbooking.mobile.domain.repository.AuthRepository
import com.hotelbooking.mobile.model.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var message by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    fun resetState() {
        email = ""
        password = ""
        message = ""
    }

    fun login(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            message = "Vă rugăm să completați toate câmpurile"
            return
        }

        viewModelScope.launch {
            isLoading = true
            message = "Se autentifică..."
            val result = repository.login(LoginRequest(email, password))
            isLoading = false
            
            result.onSuccess {
                message = "✅ Autentificare reușită!"
                onSuccess()
            }.onFailure {
                message = "❌ Eroare: ${it.message}"
            }
        }
    }
}
