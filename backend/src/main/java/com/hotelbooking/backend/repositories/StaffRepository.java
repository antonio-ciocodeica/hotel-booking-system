package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StaffRepository extends JpaRepository<StaffEntity, UUID> {
	Optional<StaffEntity> findByEmail(String email);
}
