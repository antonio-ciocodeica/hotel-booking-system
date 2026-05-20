package com.hotelbooking.backend.controllers;

import com.hotelbooking.backend.domain.dto.roomtypes.RoomTypeRequest;
import com.hotelbooking.backend.domain.dto.roomtypes.RoomTypeResponse;
import com.hotelbooking.backend.domain.entities.RoomTypeEntity;
import com.hotelbooking.backend.services.RoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:5174"})
@RestController
@RequiredArgsConstructor
public class RoomTypesController {

    private final RoomTypeService roomTypeService;

    @GetMapping("/hotels/{hotelId}/room-types")
    public ResponseEntity<List<RoomTypeResponse>> getRoomTypesByHotel(@PathVariable UUID hotelId) {
        return ResponseEntity.ok(roomTypeService.getRoomTypesByHotel(hotelId));
    }

    @GetMapping("/room-types/{roomTypeId}")
    public ResponseEntity<RoomTypeResponse> getRoomTypeById(@PathVariable UUID roomTypeId) {
        return ResponseEntity.ok(roomTypeService.getRoomTypeById(roomTypeId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/hotels/{hotelId}/room-types")
    public ResponseEntity<RoomTypeResponse> createRoomType(
            @PathVariable UUID hotelId,
            @Valid @RequestBody RoomTypeRequest request
    ) {
        RoomTypeResponse response = roomTypeService.createRoomType(hotelId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping(value = "/room-types/{roomTypeId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RoomTypeResponse> uploadRoomTypeImages(
            @PathVariable UUID roomTypeId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(roomTypeService.addImages(roomTypeId, files));
    }

    @GetMapping("/room-types")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<RoomTypeResponse>> getAllRoomTypes() {
        return ResponseEntity.ok(roomTypeService.findAll());
    }

}