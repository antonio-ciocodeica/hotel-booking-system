package com.hotelbooking.backend.domain.dto.authentication;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AvailabilityResponse {
    private UUID roomId;
    private boolean available;
    private LocalDate availableFrom;
}
