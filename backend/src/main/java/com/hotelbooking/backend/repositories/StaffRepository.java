package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, UUID> {
	Optional<StaffEntity> findByEmail(String email);

	List<StaffEntity> findByAccountStatus(Integer accountStatus);
}