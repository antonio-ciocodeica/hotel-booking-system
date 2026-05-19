package com.hotelbooking.mobile.domain.repository

import com.hotelbooking.mobile.domain.model.Hotel
import com.hotelbooking.mobile.domain.model.Room
import java.time.LocalDate
import java.util.UUID

interface HotelRepository {
    suspend fun searchHotels(location: String, checkIn: LocalDate, checkOut: LocalDate): Result<List<Hotel>>
    suspend fun getRoomsForHotel(hotelId: UUID, checkIn: LocalDate, checkOut: LocalDate): Result<List<Room>>
}
