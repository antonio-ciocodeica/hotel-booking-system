package com.hotelbooking.mobile.domain.model

import java.util.UUID

data class Room(
    val id: UUID,
    val hotelId: UUID,
    val roomName: String,
    val roomFacilities: String?,
    val roomNumber: Int,
    val status: Int, // 0 = available, 1 = occupied, 2 = unavailable
    val childCapacity: Int?,
    val adultCapacity: Int?,
    val basePrice: Double
)
