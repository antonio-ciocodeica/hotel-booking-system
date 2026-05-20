package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.RoomTypeEntity;
import com.hotelbooking.backend.repositories.projections.RoomTypeAvailabilityProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity, UUID> {

	List<RoomTypeEntity> findAllByHotel_IdOrderByRoomNameAsc(UUID hotelId);

	boolean existsByHotel_IdAndRoomNameIgnoreCase(UUID hotelId, String roomName);

	/**
	 * Returns availability per room type for a given hotel and date interval.
	 * A room is considered unavailable if it has an overlapping booking in the given statuses.
	 */
	@Query(value = """
		SELECT
			rt.id AS roomTypeId,
			rt.room_name AS roomName,
			rt.room_facilities AS roomFacilities,
			rt.child_capacity AS childCapacity,
			rt.adult_capacity AS adultCapacity,
			rt.base_price AS basePrice,
			(
				COUNT(DISTINCT r.id) - COUNT(DISTINCT b.room_id)
			) AS availableRooms
		FROM room_types rt
		JOIN rooms r ON r.room_type_id = rt.id
		LEFT JOIN bookings b ON b.room_id = r.id
			AND b.status IN (:statuses)
			AND b.check_in < :checkOut
			AND b.check_out > :checkIn
		WHERE rt.hotel_id = :hotelId
			AND r.room_status <> 2
		GROUP BY rt.id, rt.room_name, rt.room_facilities, rt.child_capacity, rt.adult_capacity, rt.base_price
		HAVING (COUNT(DISTINCT r.id) - COUNT(DISTINCT b.room_id)) > 0
		ORDER BY rt.room_name ASC
	""", nativeQuery = true)
	List<RoomTypeAvailabilityProjection> findRoomTypeAvailabilityByHotel(
			@Param("hotelId") UUID hotelId,
			@Param("checkIn") LocalDate checkIn,
			@Param("checkOut") LocalDate checkOut,
			@Param("statuses") List<Integer> statuses
	);
}