package com.hotelbooking.backend.services.impl;

import com.hotelbooking.backend.domain.dto.rooms.RoomRequest;
import com.hotelbooking.backend.domain.dto.rooms.RoomResponse;
import com.hotelbooking.backend.domain.entities.RoomEntity;
import com.hotelbooking.backend.domain.entities.RoomTypeEntity;
import com.hotelbooking.backend.domain.entities.StaffEntity;
import com.hotelbooking.backend.repositories.RoomRepository;
import com.hotelbooking.backend.repositories.RoomTypeRepository;
import com.hotelbooking.backend.repositories.StaffRepository;
import com.hotelbooking.backend.services.RoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final StaffRepository staffRepository;

    @Override
    public RoomResponse createRoom(UUID roomTypeId, RoomRequest request) {
        StaffEntity staff = getAuthenticatedStaffOrThrow();

        RoomTypeEntity roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Room type not found"));

        UUID staffHotelId = staff.getHotel() != null ? staff.getHotel().getId() : null;
        UUID roomTypeHotelId = roomType.getHotel() != null ? roomType.getHotel().getId() : null;
        System.out.println("DEBUG: Staff email: " + staff.getEmail());
        System.out.println("DEBUG: Staff hotel ID: " + (staff.getHotel() != null ? staff.getHotel().getId() : "NULL"));
        System.out.println("DEBUG: RoomType hotel ID: " + roomTypeHotelId);
        System.out.println("DEBUG: RoomType ID: " + roomTypeId);
        System.out.println("DEBUG: RoomRequest: " + request);

        if (roomTypeHotelId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Room type is not associated with any hotel");
        }
        if (roomTypeHotelId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Room type is not associated with any hotel");
        }

        if (staffHotelId != null && !staffHotelId.equals(roomTypeHotelId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only create rooms for your hotel. Staff Hotel ID: " + staffHotelId + " RoomType Hotel ID: " + roomTypeHotelId);
        }

        if (roomRepository.existsByRoomType_IdAndRoomNumber(roomTypeId, request.getRoomNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Room number already exists for this room type");
        }

        RoomEntity room = new RoomEntity();
        room.setRoomType(roomType);
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomStatus(request.getRoomStatus() == null ? 0 : request.getRoomStatus());

        RoomEntity saved = roomRepository.save(room);

        System.out.println("DEBUG: RoomEntity saved id: " + saved.getId());

        return new RoomResponse(
                saved.getId(),
                saved.getRoomType().getId(),
                saved.getRoomNumber(),
                saved.getRoomStatus()
        );
    }

    private StaffEntity getAuthenticatedStaffOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        return staffRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Only staff members can perform this action"));
    }
}
