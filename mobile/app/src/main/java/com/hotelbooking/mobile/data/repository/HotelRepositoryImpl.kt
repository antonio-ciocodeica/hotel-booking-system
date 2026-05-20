package com.hotelbooking.mobile.data.repository

import com.hotelbooking.mobile.data.api.HotelApi
import com.hotelbooking.mobile.domain.model.Hotel
import com.hotelbooking.mobile.domain.model.Room
import com.hotelbooking.mobile.domain.model.RoomType
import com.hotelbooking.mobile.domain.repository.HotelRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class HotelRepositoryImpl(
    private val hotelApi: HotelApi
) : HotelRepository {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override suspend fun searchHotels(location: String, checkIn: LocalDate, checkOut: LocalDate): Result<List<Hotel>> {
        return try {
            val response = hotelApi.searchHotels(
                location,
                checkIn.format(formatter),
                checkOut.format(formatter)
            )
            if (response.isSuccessful) {
                val hotels = response.body()?.map {
                    Hotel(it.id, it.name, it.location, it.amenities, it.description)
                } ?: emptyList()
                Result.success(hotels)
            } else {
                Result.failure(Exception("Search failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRoomTypesAvailability(hotelId: UUID, checkIn: LocalDate, checkOut: LocalDate): Result<List<RoomType>> {
        return try {
            val response = hotelApi.getRoomTypesAvailability(
                hotelId,
                checkIn.format(formatter),
                checkOut.format(formatter)
            )
            if (response.isSuccessful) {
                val roomTypes = response.body()?.map { rt ->
                    RoomType(
                        rt.id,
                        hotelId,
                        rt.roomName,
                        rt.roomFacilities,
                        rt.childCapacity,
                        rt.adultCapacity,
                        rt.basePrice,
                        rt.imageUrls?.map { "http://10.0.2.2:8080$it" } ?: emptyList()
                    )
                } ?: emptyList()
                Result.success(roomTypes)
            } else {
                Result.failure(Exception("Get room types failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRoomsByRoomType(roomTypeId: UUID, checkIn: LocalDate, checkOut: LocalDate): Result<List<Room>> {
        return try {
            val response = hotelApi.getRoomsByRoomType(
                roomTypeId,
                checkIn.format(formatter),
                checkOut.format(formatter)
            )
            if (response.isSuccessful) {
                val rooms = response.body()?.map {
                    Room(
                        it.id ?: UUID.randomUUID(),
                        it.roomTypeId ?: roomTypeId,
                        it.roomNumber ?: 0,
                        it.roomStatus ?: 0
                    )
                } ?: emptyList()
                Result.success(rooms)
            } else {
                Result.failure(Exception("Get rooms failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
