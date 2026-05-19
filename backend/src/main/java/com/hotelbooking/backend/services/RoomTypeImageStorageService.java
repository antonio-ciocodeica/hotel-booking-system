package com.hotelbooking.backend.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface RoomTypeImageStorageService {

	/**
	 * Stores an image file on disk and returns the public URL (e.g. /uploads/room-types/{id}/{file}).
	 */
	String storeRoomTypeImage(UUID roomTypeId, MultipartFile file);
}


