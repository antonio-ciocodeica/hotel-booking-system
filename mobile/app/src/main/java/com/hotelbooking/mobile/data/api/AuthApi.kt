package com.hotelbooking.mobile.data.api

import com.hotelbooking.mobile.model.AuthResponse
import com.hotelbooking.mobile.model.LoginRequest
import com.hotelbooking.mobile.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Unit>
}