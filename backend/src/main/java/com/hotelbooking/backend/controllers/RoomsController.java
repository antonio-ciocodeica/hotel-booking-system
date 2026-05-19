package com.hotelbooking.backend.controllers;

import com.hotelbooking.backend.domain.dto.rooms.RoomRequest;
import com.hotelbooking.backend.domain.dto.rooms.RoomResponse;
import com.hotelbooking.backend.services.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:5174"})
@RestController
@RequiredArgsConstructor
public class RoomsController {

    private final RoomService roomService;


    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/room-types/{roomTypeId}/rooms")
    public ResponseEntity<RoomResponse> createRoom(
            @PathVariable UUID roomTypeId,
            @Valid @RequestBody RoomRequest request
    ) {
        RoomResponse response = roomService.createRoom(roomTypeId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}