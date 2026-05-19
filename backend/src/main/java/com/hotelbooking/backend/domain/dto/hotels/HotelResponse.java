package com.hotelbooking.backend.domain.dto.hotels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class HotelResponse {

    private UUID id;
    private String name;
    private String location;
    private String facilities;
    private String description;
}

