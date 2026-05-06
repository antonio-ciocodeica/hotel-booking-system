package com.hotelbooking.backend.domain.dto.authentication;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
@Data
public class BookingResponse {
    private UUID id;
    private UUID userId;
    private UUID roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer status;
    private BigDecimal price;
    private LocalDate reservationDate;
}
