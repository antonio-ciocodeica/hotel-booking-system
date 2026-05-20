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

import java.util.List;
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

        if (roomTypeHotelId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Room type is not associated with any hotel");
        }

        if (staff.getRole() != 2 && staffHotelId != null && !staffHotelId.equals(roomTypeHotelId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Staff can only create rooms for their assigned hotel");
        }

        // --- VERIFICAREA NOUĂ ȘI CORECTĂ ---
        if (roomRepository.existsByRoomType_Hotel_IdAndRoomNumber(roomTypeHotelId, request.getRoomNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Room number " + request.getRoomNumber() + " already exists in this hotel!");
        }
        // -----------------------------------

        RoomEntity room = new RoomEntity();
        room.setRoomType(roomType);
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomStatus(request.getRoomStatus() == null ? 0 : request.getRoomStatus());

        RoomEntity saved = roomRepository.save(room);

        return new RoomResponse(
                saved.getId(),
                saved.getRoomType().getId(),
                saved.getRoomNumber(),
                saved.getRoomStatus()
        );
    }

    @Override
    public List<RoomResponse> getRoomsByRoomType(UUID roomTypeId) {
        // Listing rooms is allowed for authenticated users; if you need hotel-based access control,
        // enforce it at the controller/service layer similarly to createRoom.
        return roomRepository.findAllByRoomType_IdOrderByRoomNumberAsc(roomTypeId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RoomResponse getRoomById(UUID roomId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        return toResponse(room);
    }

    private RoomResponse toResponse(RoomEntity saved) {
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
