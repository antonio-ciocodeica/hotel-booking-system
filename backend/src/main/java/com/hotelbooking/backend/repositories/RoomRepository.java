package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {
	boolean existsByRoomType_IdAndRoomNumber(UUID roomTypeId, Integer roomNumber);

	List<RoomEntity> findAllByRoomType_IdOrderByRoomNumberAsc(UUID roomTypeId);
}
