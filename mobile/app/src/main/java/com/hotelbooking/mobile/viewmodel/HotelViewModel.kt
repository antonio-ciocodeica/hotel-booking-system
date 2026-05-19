package com.hotelbooking.mobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotelbooking.mobile.domain.model.Hotel
import com.hotelbooking.mobile.domain.model.Room
import com.hotelbooking.mobile.domain.repository.HotelRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class HotelViewModel(private val repository: HotelRepository) : ViewModel() {

    var hotels by mutableStateOf<List<Hotel>>(emptyList())
    var rooms by mutableStateOf<List<Room>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    var searchLocation by mutableStateOf("")
    var checkInDate by mutableStateOf(LocalDate.now())
    var checkOutDate by mutableStateOf(LocalDate.now().plusDays(2))

    fun searchHotels(location: String, checkIn: LocalDate, checkOut: LocalDate, onResponse: () -> Unit) {
        searchLocation = location
        checkInDate = checkIn
        checkOutDate = checkOut
        
        viewModelScope.launch {
            isLoading = true
            error = null
            val result = repository.searchHotels(location, checkIn, checkOut)
            isLoading = false
            result.onSuccess {
                hotels = it
                error = null
            }.onFailure {
                error = it.message ?: "Eroare necunoscută la căutare"
            }
            onResponse() // Navigate regardless of success or failure
        }
    }

    fun loadRooms(hotelId: UUID) {
        viewModelScope.launch {
            isLoading = true
            val result = repository.getRoomsForHotel(hotelId, checkInDate, checkOutDate)
            isLoading = false
            result.onSuccess {
                rooms = it
                error = null
            }.onFailure {
                error = it.message
            }
        }
    }
}
