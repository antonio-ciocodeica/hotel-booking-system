package com.hotelbooking.mobile.network

import android.content.Context
import com.hotelbooking.mobile.data.api.AuthApi
import com.hotelbooking.mobile.data.api.BookingApi
import com.hotelbooking.mobile.data.api.HotelApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"
    private var tokenManager: TokenManager? = null

    fun init(context: Context) {
        tokenManager = TokenManager(context)
    }

    fun getTokenManager(): TokenManager {
        return tokenManager ?: throw IllegalStateException("TokenManager not initialized")
    }

    private val okHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val interceptor = try {
            AuthInterceptor(getTokenManager())
        } catch (e: Exception) {
            // Fallback interceptor if TokenManager is not ready
            AuthInterceptor(TokenManager(null))
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(interceptor)
            .build()
    }

    val api: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthApi::class.java)
    }

    val bookingApi: BookingApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(BookingApi::class.java)
    }

    val hotelApi: HotelApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(HotelApi::class.java)
    }
}
