package com.hotelbooking.backend.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Data
@ToString(exclude = {"staff"})
public class AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffEntity staff;

    @Column(nullable = false)
    private Integer actionType;

    @Column(nullable = false)
    private Long timestamp;

    @Column(nullable = false)
    private String details;

}
