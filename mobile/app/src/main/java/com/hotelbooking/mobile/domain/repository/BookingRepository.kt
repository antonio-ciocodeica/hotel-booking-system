package com.hotelbooking.mobile.domain.repository

import com.hotelbooking.mobile.domain.model.Booking
import java.time.LocalDate
import java.util.UUID

interface BookingRepository {
    suspend fun checkAvailability(roomId: UUID, checkIn: LocalDate, checkOut: LocalDate): Result<Boolean>
    suspend fun createBooking(roomId: UUID, checkIn: LocalDate, checkOut: LocalDate): Result<Booking>
    suspend fun getMyBookings(): Result<List<Booking>>
    suspend fun cancelBooking(bookingId: UUID): Result<Unit>
}
