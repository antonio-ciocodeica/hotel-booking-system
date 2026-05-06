package com.hotelbooking.backend.controllers;

import com.hotelbooking.backend.domain.dto.authentication.AvailabilityResponse;
import com.hotelbooking.backend.domain.dto.authentication.BookingRequest;
import com.hotelbooking.backend.domain.dto.authentication.BookingResponse;
import com.hotelbooking.backend.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingsController{

    private final BookingService bookingService;

    @PostMapping("/availability")
    public ResponseEntity<AvailabilityResponse> checkAvailability(@RequestBody BookingRequest request) {
        AvailabilityResponse response = bookingService.checkAvailability(
                request.getRoomId(),
                request.getCheckIn(),
                request.getCheckOut()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable UUID id) {
        BookingResponse response = bookingService.cancelBooking(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @GetMapping("/my/history")
    public ResponseEntity<List<BookingResponse>> getMyBookingHistory() {
        return ResponseEntity.ok(bookingService.getMyBookingHistory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getRezervare(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.getBookingByCode(id));
    }

    @PostMapping("/{id}/check-in")
    public ResponseEntity<BookingResponse> checkInRezervare(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.performCheckIn(id));
    }
}
