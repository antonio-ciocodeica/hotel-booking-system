package com.hotelbooking.mobile.data.api

import com.hotelbooking.mobile.domain.model.Booking
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDate
import java.util.UUID

interface BookingApi {

    @POST("bookings/availability")
    suspend fun checkAvailability(
        @Body request: BookingAvailabilityRequest
    ): Response<AvailabilityResponse>

    @POST("bookings")
    suspend fun createBooking(
        @Body request: BookingRequest
    ): Response<BookingResponse>

    @GET("bookings/my")
    suspend fun getMyBookings(): Response<List<BookingResponse>>

    @DELETE("bookings/{id}")
    suspend fun cancelBooking(
        @Path("id") id: UUID
    ): Response<BookingResponse>
}

data class BookingAvailabilityRequest(
    val roomId: UUID,
    val checkIn: String, // LocalDate as ISO string
    val checkOut: String
)

data class AvailabilityResponse(
    val roomId: UUID,
    val available: Boolean,
    val availableFrom: String?
)

data class BookingRequest(
    val roomId: UUID,
    val checkIn: String,
    val checkOut: String
)

data class BookingResponse(
    val id: UUID,
    val roomId: UUID,
    val checkIn: String,
    val checkOut: String,
    val status: Int,
    val price: Double
)
