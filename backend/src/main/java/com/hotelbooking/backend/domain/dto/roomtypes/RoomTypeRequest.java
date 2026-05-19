package com.hotelbooking.backend.domain.dto.roomtypes;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeRequest {

    @NotBlank
    private String roomName;

    @NotBlank
    private String roomFacilities;

    @NotNull
    @Min(0)
    private Integer childCapacity;

    @NotNull
    @Min(0)
    private Integer adultCapacity;

    @NotNull
    private BigDecimal basePrice;
}

