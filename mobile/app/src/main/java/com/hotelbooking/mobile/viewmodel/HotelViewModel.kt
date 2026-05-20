package com.hotelbooking.mobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotelbooking.mobile.domain.model.Hotel
import com.hotelbooking.mobile.domain.model.Room
import com.hotelbooking.mobile.domain.model.RoomType
import com.hotelbooking.mobile.domain.repository.HotelRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class HotelViewModel(private val repository: HotelRepository) : ViewModel() {

    var hotels by mutableStateOf<List<Hotel>>(emptyList())
    var roomTypes by mutableStateOf<List<RoomType>>(emptyList())
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
        
        refreshSearch(onResponse)
    }

    fun refreshSearch(onResponse: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            error = null
            val result = repository.searchHotels(searchLocation, checkInDate, checkOutDate)
            isLoading = false
            result.onSuccess {
                hotels = it
                error = null
            }.onFailure {
                error = it.message ?: "Eroare necunoscută la căutare"
            }
            onResponse()
        }
    }

    fun loadRoomTypes(hotelId: UUID) {
        viewModelScope.launch {
            isLoading = true
            val result = repository.getRoomTypesAvailability(hotelId, checkInDate, checkOutDate)
            isLoading = false
            result.onSuccess {
                roomTypes = it
                error = null
            }.onFailure {
                error = it.message
            }
        }
    }

    fun loadRooms(roomTypeId: UUID) {
        viewModelScope.launch {
            isLoading = true
            val result = repository.getRoomsByRoomType(roomTypeId, checkInDate, checkOutDate)
            isLoading = false
            result.onSuccess {
                rooms = it
                error = null
            }.onFailure {
                error = it.message
            }
        }
    }

    fun clearRooms() {
        rooms = emptyList()
    }
}
