package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {

	List<RoomEntity> findAllByRoomType_IdOrderByRoomNumberAsc(UUID roomTypeId);

	boolean existsByRoomType_IdAndRoomNumber(UUID roomTypeId, Integer roomNumber);

	boolean existsByRoomType_Hotel_IdAndRoomNumber(UUID hotelId, Integer roomNumber);
}