package com.hotelbooking.mobile.network

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String, expiresInSec: Long) {
        val expirationTimestamp = System.currentTimeMillis() + (expiresInSec * 1000)
        prefs.edit()
            .putString("jwt_token", token)
            .putLong("token_expiration", expirationTimestamp)
            .apply()
    }

    fun getToken(): String? {
        val token = prefs.getString("jwt_token", null) ?: return null
        val expiration = prefs.getLong("token_expiration", 0L)
        
        if (System.currentTimeMillis() < expiration) {
            return token
        }
        
        clearToken() // Auto-clear if expired
        return null
    }

    fun clearToken() {
        prefs.edit().remove("jwt_token").remove("token_expiration").apply()
    }
}
