package com.hotelbooking.backend.services;

import com.hotelbooking.backend.domain.dto.rooms.RoomRequest;
import com.hotelbooking.backend.domain.dto.rooms.RoomResponse;

import java.util.List;
import java.util.UUID;

public interface RoomService {

    RoomResponse createRoom(UUID roomTypeId, RoomRequest request);

    List<RoomResponse> getRoomsByRoomType(UUID roomTypeId);

    RoomResponse getRoomById(UUID roomId);
}

