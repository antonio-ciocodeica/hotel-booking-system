package com.hotelbooking.backend.controllers;

import com.hotelbooking.backend.domain.dto.hotels.HotelRequest;
import com.hotelbooking.backend.domain.dto.hotels.HotelResponse;
import com.hotelbooking.backend.domain.entities.HotelEntity;
import com.hotelbooking.backend.domain.entities.RoomTypeEntity;
import com.hotelbooking.backend.repositories.HotelRepository;
import com.hotelbooking.backend.repositories.RoomTypeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:5174"})
@RestController
@RequestMapping(path = "/hotels")
@RequiredArgsConstructor
public class HotelsController {

	private final HotelRepository hotelRepository;
	private final RoomTypeRepository roomTypeRepository;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<HotelResponse> createHotel(@Valid @RequestBody HotelRequest request) {
		HotelEntity entity = new HotelEntity();
		entity.setName(request.getName());
		entity.setLocation(request.getLocation());
		entity.setFacilities(request.getFacilities());
		entity.setDescription(request.getDescription());

		HotelEntity saved = hotelRepository.save(entity);

		return new ResponseEntity<>(toResponse(saved), HttpStatus.CREATED);
	}

	// AICI E MODIFICAREA: Returnăm HotelResponse, nu HotelEntity
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ResponseEntity<List<HotelResponse>> getAllHotels() {
		List<HotelResponse> hotels = hotelRepository.findAll().stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
		return ResponseEntity.ok(hotels);
	}

	// Funcție ajutătoare pentru transformare Entity -> Response (DTO)
	private HotelResponse toResponse(HotelEntity saved) {
		return new HotelResponse(
				saved.getId(),
				saved.getName(),
				saved.getLocation(),
				saved.getFacilities(),
				saved.getDescription()
		);
	}
}