package com.hotelbooking.mobile.domain.model

import java.util.UUID

data class Hotel(
    val id: UUID,
    val name: String,
    val location: String,
    val facilities: String?,
    val description: String?
)
