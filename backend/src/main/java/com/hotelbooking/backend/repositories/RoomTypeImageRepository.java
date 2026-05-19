package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.RoomTypeImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomTypeImageRepository extends JpaRepository<RoomTypeImageEntity, UUID> {
    List<RoomTypeImageEntity> findAllByRoomType_IdOrderBySortOrderAsc(UUID roomTypeId);
}


