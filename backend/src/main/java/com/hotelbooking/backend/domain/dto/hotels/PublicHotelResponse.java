package com.hotelbooking.backend.domain.dto.hotels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * Public-facing hotel response used by the unauthenticated hotel search endpoint.
 *
 * Note: field names are optimized for client usage and can differ from the internal/admin DTOs.
 */
@Data
@AllArgsConstructor
public class PublicHotelResponse {
    private UUID id;
    private String name;
    private String location;

    /**
     * Amenities/facilities offered by the hotel.
     */
    private String amenities;

    private String description;
}

