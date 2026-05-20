package com.hotelbooking.backend.services.impl;

import com.hotelbooking.backend.domain.dto.roomtypes.RoomTypeRequest;
import com.hotelbooking.backend.domain.dto.roomtypes.RoomTypeResponse;
import com.hotelbooking.backend.domain.entities.HotelEntity;
import com.hotelbooking.backend.domain.entities.RoomTypeEntity;
import com.hotelbooking.backend.domain.entities.RoomTypeImageEntity;
import com.hotelbooking.backend.domain.entities.StaffEntity;
import com.hotelbooking.backend.repositories.HotelRepository;
import com.hotelbooking.backend.repositories.RoomTypeImageRepository;
import com.hotelbooking.backend.repositories.RoomTypeRepository;
import com.hotelbooking.backend.repositories.StaffRepository;
import com.hotelbooking.backend.services.RoomTypeImageStorageService;
import com.hotelbooking.backend.services.RoomTypeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeImageRepository roomTypeImageRepository;
    private final StaffRepository staffRepository;
    private final RoomTypeImageStorageService roomTypeImageStorageService;

    @Override
    public RoomTypeResponse createRoomType(UUID hotelId, RoomTypeRequest request) {
        StaffEntity staff = getAuthenticatedStaffOrThrow();

        if (staff.getRole() != 2 && staff.getHotel() != null && !staff.getHotel().getId().equals(hotelId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Staff can only create room types for their assigned hotel");
        }

        if (roomTypeRepository.existsByHotel_IdAndRoomNameIgnoreCase(hotelId, request.getRoomName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A room type with the name '" + request.getRoomName() + "' already exists in this hotel!");
        }

        HotelEntity hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        RoomTypeEntity entity = new RoomTypeEntity();
        entity.setHotel(hotel);
        entity.setRoomName(request.getRoomName());
        entity.setRoomFacilities(request.getRoomFacilities());
        entity.setChildCapacity(request.getChildCapacity());
        entity.setAdultCapacity(request.getAdultCapacity());
        entity.setBasePrice(request.getBasePrice());

        RoomTypeEntity saved = roomTypeRepository.save(entity);

        return toResponse(saved);
    }

    @Override
    public List<RoomTypeResponse> findAll() {
        return roomTypeRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RoomTypeResponse addImages(UUID roomTypeId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No files provided");
        }

        StaffEntity staff = getAuthenticatedStaffOrThrow();

        RoomTypeEntity roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Room type not found"));

        UUID hotelId = roomType.getHotel() != null ? roomType.getHotel().getId() : null;
        if (hotelId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Room type is not associated with any hotel");
        }

        if (staff.getHotel() != null && !hotelId.equals(staff.getHotel().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only add images to room types of your hotel");
        }

        int sortOrderStart = roomTypeImageRepository.findAllByRoomType_IdOrderBySortOrderAsc(roomTypeId).size();

        int i = 0;
        for (MultipartFile file : files) {
            String url = roomTypeImageStorageService.storeRoomTypeImage(roomTypeId, file);

            RoomTypeImageEntity img = new RoomTypeImageEntity();
            img.setRoomType(roomType);
            img.setUrl(url);
            img.setSortOrder(sortOrderStart + i);
            roomTypeImageRepository.save(img);
            i++;
        }

        return toResponse(roomType);
    }

    @Override
    public List<RoomTypeResponse> findAllAsResponse() {
        return roomTypeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<RoomTypeResponse> getRoomTypesByHotel(UUID hotelId) {
        return roomTypeRepository.findAllByHotel_IdOrderByRoomNameAsc(hotelId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RoomTypeResponse getRoomTypeById(UUID roomTypeId) {
        RoomTypeEntity roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Room type not found"));
        return toResponse(roomType);
    }

    private StaffEntity getAuthenticatedStaffOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        return staffRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Only staff members can perform this action"));
    }

    private RoomTypeResponse toResponse(RoomTypeEntity entity) {
        List<String> urls = roomTypeImageRepository.findAllByRoomType_IdOrderBySortOrderAsc(entity.getId())
                .stream()
                .map(RoomTypeImageEntity::getUrl)
                .toList();

        return new RoomTypeResponse(
                entity.getId(),
                entity.getHotel().getId(),
                entity.getRoomName(),
                entity.getRoomFacilities(),
                entity.getChildCapacity(),
                entity.getAdultCapacity(),
                entity.getBasePrice(),
                urls
        );
    }
}
