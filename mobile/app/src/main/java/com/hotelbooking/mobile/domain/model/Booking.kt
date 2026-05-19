package com.hotelbooking.mobile.domain.model

import java.time.LocalDate
import java.util.UUID

data class Booking(
    val id: UUID,
    val roomId: UUID,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val status: Int, // 0 = PENDING, 1 = CONFIRMED, 2 = COMPLETED, 3 = CANCELED
    val price: Double
)
