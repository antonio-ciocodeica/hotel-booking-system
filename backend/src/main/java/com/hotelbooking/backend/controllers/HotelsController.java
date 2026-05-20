package com.hotelbooking.backend.controllers;

import com.hotelbooking.backend.domain.dto.hotels.HotelRequest;
import com.hotelbooking.backend.domain.dto.hotels.HotelResponse;
import com.hotelbooking.backend.domain.dto.hotels.PublicHotelResponse;
import com.hotelbooking.backend.domain.dto.roomtypes.RoomTypeAvailabilityResponse;
import com.hotelbooking.backend.domain.entities.HotelEntity;
import com.hotelbooking.backend.domain.entities.RoomTypeImageEntity;
import com.hotelbooking.backend.repositories.HotelRepository;
import com.hotelbooking.backend.repositories.RoomTypeImageRepository;
import com.hotelbooking.backend.repositories.RoomTypeRepository;
import com.hotelbooking.backend.repositories.projections.RoomTypeAvailabilityProjection;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
	private final RoomTypeImageRepository roomTypeImageRepository;

	private static final List<Integer> ACTIVE_BOOKING_STATUSES = List.of(0, 1);

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

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ResponseEntity<List<HotelResponse>> getAllHotels() {
		List<HotelResponse> hotels = hotelRepository.findAll().stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
		return ResponseEntity.ok(hotels);
	}

	private HotelResponse toResponse(HotelEntity saved) {
		return new HotelResponse(
				saved.getId(),
				saved.getName(),
				saved.getLocation(),
				saved.getFacilities(),
				saved.getDescription()
		);
	}

	/**
	 * Public search endpoint.
	 * Returns hotels in the given location that have at least one available room for the date range.
	 */
	@GetMapping("/search")
	public ResponseEntity<List<PublicHotelResponse>> searchHotels(
			@RequestParam String location,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
	) {
		LocalDate in = checkIn != null ? checkIn : LocalDate.now();
		LocalDate out = checkOut != null ? checkOut : in.plusDays(1);
		validateInterval(in, out);

		if (location == null || location.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		List<PublicHotelResponse> results = hotelRepository
				.findAvailableHotelsByLocationAndDateRange(location.trim(), in, out, ACTIVE_BOOKING_STATUSES)
				.stream()
				.map(h -> new PublicHotelResponse(
						h.getId(),
						h.getName(),
						h.getLocation(),
						h.getFacilities(),
						h.getDescription()
				))
				.toList();

		return ResponseEntity.ok(results);
	}

	/**
	 * Public availability endpoint.
	 * Returns room types within the hotel that have at least one available room in the requested interval.
	 */
	@GetMapping("/{hotelId}/availability")
	public ResponseEntity<List<RoomTypeAvailabilityResponse>> getHotelAvailability(
			@PathVariable UUID hotelId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
	) {
		LocalDate in = checkIn != null ? checkIn : LocalDate.now();
		LocalDate out = checkOut != null ? checkOut : in.plusDays(1);
		validateInterval(in, out);

		// Make sure the hotel exists (nicer 404 than returning empty array for invalid IDs)
		if (!hotelRepository.existsById(hotelId)) {
			return ResponseEntity.notFound().build();
		}

		List<RoomTypeAvailabilityProjection> rows = roomTypeRepository
				.findRoomTypeAvailabilityByHotel(hotelId, in, out, ACTIVE_BOOKING_STATUSES);

		List<RoomTypeAvailabilityResponse> responses = rows.stream().map(row -> {
			List<String> urls = roomTypeImageRepository
					.findAllByRoomType_IdOrderBySortOrderAsc(row.getRoomTypeId())
					.stream()
					.map(RoomTypeImageEntity::getUrl)
					.toList();

			return new RoomTypeAvailabilityResponse(
					row.getRoomTypeId(),
					row.getRoomName(),
					row.getRoomFacilities(),
					row.getChildCapacity(),
					row.getAdultCapacity(),
					row.getBasePrice(),
					row.getAvailableRooms(),
					urls
			);
		}).toList();

		return ResponseEntity.ok(responses);
	}

	private static void validateInterval(LocalDate checkIn, LocalDate checkOut) {
		if (checkIn == null || checkOut == null || !checkIn.isBefore(checkOut)) {
			throw new IllegalArgumentException("checkIn must be before checkOut");
		}
	}
}