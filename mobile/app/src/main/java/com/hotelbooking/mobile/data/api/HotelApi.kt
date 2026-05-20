package com.hotelbooking.mobile.data.api

import com.google.gson.annotations.SerializedName
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

    @GET("hotels/{id}/availability")
    suspend fun getRoomTypesAvailability(
        @Path("id") hotelId: UUID,
        @Query("checkIn") checkIn: String,
        @Query("checkOut") checkOut: String
    ): Response<List<RoomTypeResponse>>

    @GET("room-types/{id}/rooms")
    suspend fun getRoomsByRoomType(
        @Path("id") roomTypeId: UUID,
        @Query("checkIn") checkIn: String,
        @Query("checkOut") checkOut: String
    ): Response<List<RoomResponse>>
}

data class RoomTypeResponse(
    @SerializedName("roomTypeId")
    val id: UUID,
    val roomName: String,
    val roomFacilities: String?,
    val childCapacity: Int?,
    val adultCapacity: Int?,
    val basePrice: Double,
    val imageUrls: List<String>? = null
)

data class HotelResponse(
    val id: UUID,
    val name: String,
    val location: String,
    val amenities: String?,
    val description: String?
)

data class RoomResponse(
    val id: UUID?,
    val roomTypeId: UUID?,
    val roomNumber: Int?,
    val roomStatus: Int?
)
