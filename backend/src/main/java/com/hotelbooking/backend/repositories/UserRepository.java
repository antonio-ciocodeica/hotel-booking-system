package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByName(String username);

    Optional<Object> findByEmail(String email);

    boolean existsByEmail(String username);
}
