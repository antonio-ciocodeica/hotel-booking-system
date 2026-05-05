package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HotelRepository extends JpaRepository<HotelEntity, UUID> {
}
