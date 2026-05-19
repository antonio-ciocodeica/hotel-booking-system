package com.hotelbooking.mobile.data.repository

import com.hotelbooking.mobile.data.api.BookingApi
import com.hotelbooking.mobile.data.api.BookingAvailabilityRequest
import com.hotelbooking.mobile.data.api.BookingRequest
import com.hotelbooking.mobile.domain.model.Booking
import com.hotelbooking.mobile.domain.repository.BookingRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class BookingRepositoryImpl(
    private val bookingApi: BookingApi
) : BookingRepository {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override suspend fun checkAvailability(roomId: UUID, checkIn: LocalDate, checkOut: LocalDate): Result<Boolean> {
        return try {
            val response = bookingApi.checkAvailability(
                BookingAvailabilityRequest(roomId, checkIn.format(formatter), checkOut.format(formatter))
            )
            if (response.isSuccessful) {
                Result.success(response.body()?.available ?: false)
            } else {
                Result.failure(Exception("Check availability failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createBooking(roomId: UUID, checkIn: LocalDate, checkOut: LocalDate): Result<Booking> {
        return try {
            val response = bookingApi.createBooking(
                BookingRequest(roomId, checkIn.format(formatter), checkOut.format(formatter))
            )
            if (response.isSuccessful) {
                val body = response.body()!!
                Result.success(Booking(
                    body.id,
                    body.roomId,
                    LocalDate.parse(body.checkIn),
                    LocalDate.parse(body.checkOut),
                    body.status,
                    body.price
                ))
            } else {
                Result.failure(Exception("Create booking failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyBookings(): Result<List<Booking>> {
        return try {
            val response = bookingApi.getMyBookings()
            if (response.isSuccessful) {
                val list = response.body()?.map { body ->
                    Booking(
                        body.id,
                        body.roomId,
                        LocalDate.parse(body.checkIn),
                        LocalDate.parse(body.checkOut),
                        body.status,
                        body.price
                    )
                } ?: emptyList()
                Result.success(list)
            } else {
                Result.failure(Exception("Get bookings failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelBooking(bookingId: UUID): Result<Unit> {
        return try {
            val response = bookingApi.cancelBooking(bookingId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Cancel booking failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
