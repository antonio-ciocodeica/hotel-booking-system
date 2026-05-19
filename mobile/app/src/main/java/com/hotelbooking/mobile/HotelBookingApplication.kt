package com.hotelbooking.mobile

import android.app.Application
import android.util.Log
import com.hotelbooking.mobile.network.RetrofitClient

class HotelBookingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("HOTEL_APP", "Application onCreate - START")
        try {
            RetrofitClient.init(this)
            Log.d("HOTEL_APP", "RetrofitClient initialized successfully")
        } catch (e: Exception) {
            Log.e("HOTEL_APP", "FAILED to initialize RetrofitClient", e)
        }
    }
}
