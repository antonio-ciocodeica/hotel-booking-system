package com.hotelbooking.mobile.data.api

import com.hotelbooking.mobile.data.api.HotelResponse
import com.hotelbooking.mobile.data.api.RoomResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface HotelApi {

    @GET("hotels/search")
    suspend fun searchHotels(
        @Query("location") location: String,
        @Query("checkIn") checkIn: String,
        @Query("checkOut") checkOut: String
    ): Response<List<HotelResponse>>

    @GET("hotels/{id}/rooms")
    suspend fun getRoomsForHotel(
        @Path("id") hotelId: UUID,
        @Query("checkIn") checkIn: String,
        @Query("checkOut") checkOut: String
    ): Response<List<RoomResponse>>
}

data class HotelResponse(
    val id: UUID,
    val name: String,
    val location: String,
    val amenities: String?,
    val description: String?
)

data class RoomResponse(
    val id: UUID,
    val hotelId: UUID,
    val type: String,
    val facilities: String?,
    val roomNumber: Int,
    val floor: Int,
    val balcony: Int,
    val maxAdults: Int,
    val price: Double
)
