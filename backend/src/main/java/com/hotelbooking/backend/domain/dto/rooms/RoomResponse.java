package com.hotelbooking.backend.domain.dto.rooms;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RoomResponse {

    private UUID id;
    private UUID roomTypeId;
    private Integer roomNumber;
    private Integer roomStatus;
}

