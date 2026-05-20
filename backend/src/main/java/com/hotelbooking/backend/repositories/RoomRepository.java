package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelbooking.backend.repositories.projections.AvailableRoomIdProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {

	List<RoomEntity> findAllByRoomType_IdOrderByRoomNumberAsc(UUID roomTypeId);

	boolean existsByRoomType_IdAndRoomNumber(UUID roomTypeId, Integer roomNumber);

	boolean existsByRoomType_Hotel_IdAndRoomNumber(UUID hotelId, Integer roomNumber);

	/**
	 * Returns all available rooms in a hotel for the given date range.
	 * A room is unavailable if it has an overlapping booking in one of the given statuses.
	 */
	@Query(value = """
		SELECT
			r.room_type_id AS roomTypeId,
			r.id AS roomId
		FROM rooms r
		JOIN room_types rt ON rt.id = r.room_type_id
		WHERE rt.hotel_id = :hotelId
			AND r.room_status <> 2
			AND NOT EXISTS (
				SELECT 1
				FROM bookings b
				WHERE b.room_id = r.id
					AND b.status IN (:statuses)
					AND b.check_in < :checkOut
					AND b.check_out > :checkIn
			)
		ORDER BY r.room_type_id, r.room_number
	""", nativeQuery = true)
	List<AvailableRoomIdProjection> findAvailableRoomIdsByHotelAndDateRange(
			@Param("hotelId") UUID hotelId,
			@Param("checkIn") LocalDate checkIn,
			@Param("checkOut") LocalDate checkOut,
			@Param("statuses") List<Integer> statuses
	);
}