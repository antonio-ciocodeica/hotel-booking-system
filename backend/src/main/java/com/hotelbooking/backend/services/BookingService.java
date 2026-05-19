package com.hotelbooking.backend.services;

import com.hotelbooking.backend.domain.dto.authentication.AvailabilityResponse;
import com.hotelbooking.backend.domain.dto.authentication.BookingRequest;
import com.hotelbooking.backend.domain.dto.authentication.BookingResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingService {
    AvailabilityResponse checkAvailability(UUID roomId, LocalDate checkIn, LocalDate checkOut);

    BookingResponse createBooking(BookingRequest request);

    BookingResponse cancelBooking(UUID bookingId);

    List<BookingResponse> getMyBookings();

    List<BookingResponse> getMyBookingHistory();

    BookingResponse getBookingByCode(UUID code);

    BookingResponse performCheckIn(UUID code);

    BookingResponse performCheckOut(UUID code);
}
