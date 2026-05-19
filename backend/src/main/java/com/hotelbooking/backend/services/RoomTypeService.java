package com.hotelbooking.backend.services;

import com.hotelbooking.backend.domain.dto.roomtypes.RoomTypeRequest;
import com.hotelbooking.backend.domain.dto.roomtypes.RoomTypeResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface RoomTypeService {

    RoomTypeResponse createRoomType(UUID hotelId, RoomTypeRequest request);

    RoomTypeResponse addImages(UUID roomTypeId, List<MultipartFile> files);

    List<RoomTypeResponse> getRoomTypesByHotel(UUID hotelId);

    RoomTypeResponse getRoomTypeById(UUID roomTypeId);
}

