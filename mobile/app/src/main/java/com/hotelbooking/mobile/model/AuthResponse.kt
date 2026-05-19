package com.hotelbooking.mobile.model

data class AuthResponse(
    val token: String,
    val expiresIn: Long
)
