package com.hotelbooking.mobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotelbooking.mobile.domain.model.Booking
import com.hotelbooking.mobile.domain.repository.BookingRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class BookingViewModel(private val repository: BookingRepository) : ViewModel() {

    var myBookings by mutableStateOf<List<Booking>>(emptyList())
    var isLoading by mutableStateOf(false)
    var bookingMessage by mutableStateOf("")

    fun resetBookingMessage() {
        bookingMessage = ""
    }

    fun loadMyBookings() {
        viewModelScope.launch {
            isLoading = true
            val result = repository.getMyBookings()
            isLoading = false
            result.onSuccess {
                myBookings = it
            }
        }
    }

    fun makeBooking(roomId: UUID, checkIn: LocalDate, checkOut: LocalDate, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            bookingMessage = "Se verifică disponibilitatea..."
            
            val availability = repository.checkAvailability(roomId, checkIn, checkOut)
            
            availability.onSuccess { isAvailable ->
                if (isAvailable) {
                    bookingMessage = "Se efectuează rezervarea..."
                    val bookingResult = repository.createBooking(roomId, checkIn, checkOut)
                    bookingResult.onSuccess {
                        bookingMessage = "✅ Rezervare confirmată!"
                        onSuccess()
                    }.onFailure {
                        bookingMessage = "❌ Eroare la rezervare: ${it.message}"
                    }
                } else {
                    bookingMessage = "❌ Camera nu este disponibilă în perioada selectată."
                }
            }.onFailure {
                bookingMessage = "❌ Eroare: ${it.message}"
            }
            
            isLoading = false
        }
    }

    fun cancelBooking(bookingId: UUID) {
        viewModelScope.launch {
            isLoading = true
            val result = repository.cancelBooking(bookingId)
            isLoading = false
            result.onSuccess {
                loadMyBookings()
            }
        }
    }
}
