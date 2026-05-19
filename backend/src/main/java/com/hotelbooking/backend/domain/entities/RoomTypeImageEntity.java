package com.hotelbooking.backend.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "room_type_images")
@Data
@ToString(exclude = {"roomType"})
public class RoomTypeImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomTypeEntity roomType;

    @Column(nullable = false)
    private String url;

    @Column(name = "sort_order")
    private Integer sortOrder;
}

