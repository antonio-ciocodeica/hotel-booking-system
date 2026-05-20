package com.hotelbooking.mobile.model

data class RoomType(
    val id: Long,
    val typeName: String,
    val description: String,
    val pricePerNight: Double,
    val capacity: Int
)
