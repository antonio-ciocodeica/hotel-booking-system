package com.hotelbooking.backend.domain.dto.authentication;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingRequest {
    private UUID roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
}