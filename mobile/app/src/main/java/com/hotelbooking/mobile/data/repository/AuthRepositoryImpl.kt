package com.hotelbooking.mobile.data.repository

import com.hotelbooking.mobile.data.api.AuthApi
import com.hotelbooking.mobile.domain.repository.AuthRepository
import com.hotelbooking.mobile.model.AuthResponse
import com.hotelbooking.mobile.model.LoginRequest
import com.hotelbooking.mobile.model.RegisterRequest
import com.hotelbooking.mobile.network.TokenManager

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = authApi.login(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    tokenManager.saveToken(body.token, body.expiresIn)
                    Result.success(body)
                } else {
                    Result.failure(Exception("Empty body"))
                }
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = authApi.register(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        tokenManager.clearToken()
    }

    override fun isLoggedIn(): Boolean {
        return tokenManager.getToken() != null
    }
}
