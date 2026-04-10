package com.hotelbooking.backend.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "room_types")
@Data
public class RoomTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private HotelEntity hotel;

    @Column(unique = true, nullable = false)
    private String roomName;

    @Column(nullable = false)
    private String roomFacilities;

    @Column(nullable = false)
    private Integer childCapacity;

    @Column(nullable = false)
    private Integer adultCapacity;

    @Column(nullable = false)
    private BigDecimal basePrice;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomEntity> rooms;

}
