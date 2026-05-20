package com.hotelbooking.mobile.domain.model

import java.util.UUID

data class Room(
    val id: UUID,
    val roomTypeId: UUID,
    val roomNumber: Int,
    val status: Int // 0 = available, 1 = occupied
)
