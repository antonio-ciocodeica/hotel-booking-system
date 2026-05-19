package com.hotelbooking.mobile.model
data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val phoneNumber: String
)