package com.hotelbooking.mobile.domain.model

import java.util.UUID

data class RoomType(
    val id: UUID,
    val hotelId: UUID,
    val roomName: String,
    val roomFacilities: String?,
    val childCapacity: Int?,
    val adultCapacity: Int?,
    val basePrice: Double,
    val imageUrls: List<String> = emptyList()
)
