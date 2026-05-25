package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {

    List<BookingEntity> findByRoomIdAndStatusInAndCheckInBeforeAndCheckOutAfter(
            UUID roomId,
            List<Integer> statuses,
            LocalDate checkIn,
            LocalDate checkOut
    );

    List<BookingEntity> findByUserIdAndStatusInOrderByCheckInAsc(
            UUID userId,
            List<Integer> statuses
    );

    List<BookingEntity> findByUserId(UUID userID);

    Optional<BookingEntity> findById(UUID bookingCode);

    List<BookingEntity> findByRoomRoomTypeHotelId(UUID hotelId);
}
