package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
