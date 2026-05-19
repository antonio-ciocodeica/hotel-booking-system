package com.hotelbooking.mobile.network

import com.hotelbooking.mobile.model.Hotel
import retrofit2.Response
import retrofit2.http.GET

interface HotelApi {

    @GET("hotels")
    suspend fun getHotels(): Response<List<Hotel>>
}