package com.hotelbooking.backend.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rooms")
@Data
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomTypeEntity roomType;

    @Column(nullable = false, unique = true)
    private Integer roomNumber;

    @Column(nullable = false)
    private Integer roomStatus;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookingEntity> bookings;

}
