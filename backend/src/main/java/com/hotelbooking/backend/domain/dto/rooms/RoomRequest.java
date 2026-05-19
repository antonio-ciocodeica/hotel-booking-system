package com.hotelbooking.backend.domain.dto.rooms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    @NotNull
    @Min(1)
    private Integer roomNumber;

    /**
     * 0 = available, 1 = occupied, 2 = unavailable
     */
    private Integer roomStatus;
}

