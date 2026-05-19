package com.hotelbooking.backend.domain.dto.roomtypes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RoomTypeResponse {

    private UUID id;
    private UUID hotelId;
    private String roomName;
    private String roomFacilities;
    private Integer childCapacity;
    private Integer adultCapacity;
    private BigDecimal basePrice;
    private List<String> imageUrls;
}

