package com.hotelbooking.backend.repositories;

import com.hotelbooking.backend.domain.entities.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLogEntity, UUID> {
}
