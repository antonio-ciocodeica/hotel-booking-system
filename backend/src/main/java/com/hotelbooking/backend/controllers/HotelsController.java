package com.hotelbooking.backend.controllers;

import com.hotelbooking.backend.domain.dto.hotels.HotelRequest;
import com.hotelbooking.backend.domain.dto.hotels.HotelResponse;
import com.hotelbooking.backend.domain.entities.HotelEntity;
import com.hotelbooking.backend.repositories.HotelRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/hotels")
@RequiredArgsConstructor
public class HotelsController {

	private final HotelRepository hotelRepository;

	/**
	 * Create a hotel (ADMIN only).
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<HotelResponse> createHotel(@Valid @RequestBody HotelRequest request) {

		HotelEntity entity = new HotelEntity();
		entity.setName(request.getName());
		entity.setLocation(request.getLocation());
		entity.setFacilities(request.getFacilities());
		entity.setDescription(request.getDescription());

		HotelEntity saved = hotelRepository.save(entity);

		HotelResponse response = new HotelResponse(
				saved.getId(),
				saved.getName(),
				saved.getLocation(),
				saved.getFacilities(),
				saved.getDescription()
		);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}


