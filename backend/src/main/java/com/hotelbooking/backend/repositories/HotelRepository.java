package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.HotelEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<HotelEntity, UUID> {

	/**
	 * Finds hotels in a location that have at least one room available in the requested interval.
	 * Availability is determined by excluding rooms with an overlapping booking in one of the given statuses.
	 */
	@Query("""
		select distinct h
		from HotelEntity h
		where lower(h.location) like lower(concat('%', :location, '%'))
		and exists (
			select 1
			from RoomEntity r
			join r.roomType rt
			where rt.hotel = h
			and r.roomStatus <> 2
			and not exists (
				select 1
				from BookingEntity b
				where b.room = r
				and b.status in :statuses
				and b.checkIn < :checkOut
				and b.checkOut > :checkIn
			)
		)
	""")
	List<HotelEntity> findAvailableHotelsByLocationAndDateRange(
			@Param("location") String location,
			@Param("checkIn") LocalDate checkIn,
			@Param("checkOut") LocalDate checkOut,
			@Param("statuses") List<Integer> statuses
	);
}
