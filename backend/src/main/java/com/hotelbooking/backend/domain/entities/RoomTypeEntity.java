package com.hotelbooking.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "room_types")
@Data
@ToString(exclude = {"hotel", "rooms", "images"})
@JsonIgnoreProperties({"hotel", "rooms", "images", "hibernateLazyInitializer", "handler"})
public class RoomTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private HotelEntity hotel;

    @Column(nullable = false)
    private String roomName;

    @Column
    private String roomFacilities;

    @Column(nullable = false)
    private Integer childCapacity;

    @Column(nullable = false)
    private Integer adultCapacity;

    @Column(nullable = false)
    private BigDecimal basePrice;

    @JsonIgnore
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomEntity> rooms;

    @JsonIgnore
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RoomTypeImageEntity> images;

}
