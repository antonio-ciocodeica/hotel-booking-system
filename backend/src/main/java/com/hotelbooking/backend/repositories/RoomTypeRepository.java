package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity, UUID> {
	List<RoomTypeEntity> findAllByHotel_IdOrderByRoomNameAsc(UUID hotelId);
}
