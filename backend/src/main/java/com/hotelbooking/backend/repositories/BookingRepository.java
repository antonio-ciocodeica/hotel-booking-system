package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
}
