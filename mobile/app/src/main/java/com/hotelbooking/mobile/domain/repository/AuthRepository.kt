package com.hotelbooking.mobile.domain.repository

import com.hotelbooking.mobile.model.AuthResponse
import com.hotelbooking.mobile.model.LoginRequest
import com.hotelbooking.mobile.model.RegisterRequest

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<AuthResponse>
    suspend fun register(request: RegisterRequest): Result<Unit>
    fun logout()
    fun isLoggedIn(): Boolean
}
