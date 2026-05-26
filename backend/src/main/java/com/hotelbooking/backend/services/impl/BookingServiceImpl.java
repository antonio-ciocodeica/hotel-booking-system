package com.hotelbooking.backend.services.impl;

import com.hotelbooking.backend.domain.dto.authentication.AvailabilityResponse;
import com.hotelbooking.backend.domain.dto.authentication.BookingRequest;
import com.hotelbooking.backend.domain.dto.authentication.BookingResponse;
import com.hotelbooking.backend.domain.entities.BookingEntity;
import com.hotelbooking.backend.domain.entities.RoomEntity;
import com.hotelbooking.backend.domain.entities.UserEntity;
import com.hotelbooking.backend.mappers.BookingMapper;
import com.hotelbooking.backend.repositories.BookingRepository;
import com.hotelbooking.backend.repositories.RoomRepository;
import com.hotelbooking.backend.services.AuthenticationService;
import com.hotelbooking.backend.services.BookingService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private RoomRepository roomRepository;
    private AuthenticationService authenticationService;
    private BookingMapper bookingMapper;

    private final List<Integer> statuses = List.of(0, 1);

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            AuthenticationService authenticationService,
            BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.authenticationService = authenticationService;
        this.bookingMapper = bookingMapper;
    }


    @Override
    public AvailabilityResponse checkAvailability(UUID roomId, LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null) checkIn = LocalDate.now();
        if (checkOut == null) checkOut = checkIn.plusDays(1);

        if (!checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("checkIn must be before checkOut!");
        }

        roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room with id " + roomId + " does not exist!"));

        List<BookingEntity> conflicts = bookingRepository
                .findByRoomIdAndStatusInAndCheckInBeforeAndCheckOutAfter(
                        roomId,
                        statuses,
                        checkOut,
                        checkIn
                );

        if (conflicts.isEmpty()) {
            return AvailabilityResponse.builder()
                    .roomId(roomId)
                    .available(true)
                    .availableFrom(checkIn)
                    .build();
        }

        LocalDate latestCheckOut = conflicts.stream()
                .map(BookingEntity::getCheckOut)
                .max(Comparator.naturalOrder())
                .orElseThrow();

        return AvailabilityResponse.builder()
                .roomId(roomId)
                .available(false)
                .availableFrom(latestCheckOut)
                .build();
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        UserEntity user = authenticationService.getUserEntityFromAuth();

        RoomEntity room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Camera nu există!"));

        List<BookingEntity> conflicts = bookingRepository.findByRoomIdAndStatusInAndCheckInBeforeAndCheckOutAfter(
                request.getRoomId(), List.of(0, 1), request.getCheckOut(), request.getCheckIn());

        if (!conflicts.isEmpty()) {
            BookingEntity conflict = conflicts.get(0);
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Room not available from " + conflict.getCheckIn() + " to " + conflict.getCheckOut()
            );
        }

        BookingEntity booking = new BookingEntity();
        booking.setRoom(room);
        booking.setUser(user);
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());

        // Calculate dynamic price based on number of nights and room base price
        long nights = java.time.temporal.ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
        if (nights <= 0) nights = 1; // Minimum 1 night charge
        
        BigDecimal basePrice = room.getRoomType().getBasePrice();
        BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(nights));
        
        booking.setPrice(totalPrice);
        booking.setStatus(0);
        booking.setReservationDate(LocalDate.now());

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse cancelBooking(UUID bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking does not exist"));

        org.springframework.security.core.Authentication auth =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        boolean isStaffOrAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().contains("STAFF") || a.getAuthority().contains("ADMIN"));

        if (!isStaffOrAdmin) {
            UserEntity user = authenticationService.getUserEntityFromAuth();
            if (booking.getUser() == null || !booking.getUser().getId().equals(user.getId())) {
                throw new SecurityException("You are not authorized to perform this action");
            }
        }

        if (booking.getStatus() == 4) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Booking cannot be canceled anymore (status: " + booking.getStatus() + ")"
            );
        }

        booking.setStatus(4);

        return bookingMapper.toDto(bookingRepository.save(booking));
    }


    @Override
    public List<BookingResponse> getMyBookings() {
        UserEntity user = authenticationService.getUserEntityFromAuth();
        List<BookingEntity> bookings = bookingRepository.findByUserIdAndStatusInOrderByCheckInAsc(user.getId(), statuses);

        return bookingMapper.toDtoList(bookings);
    }

    @Override
    public List<BookingResponse> getMyBookingHistory() {
        UserEntity user = authenticationService.getUserEntityFromAuth();

        List<BookingEntity> history = bookingRepository.findByUserId(user.getId());

        return bookingMapper.toDtoList(history);
    }

    @Override
    public BookingResponse getBookingByCode(UUID code) {
        BookingEntity booking = bookingRepository.findById(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingResponse performCheckIn(UUID code) {
        BookingEntity booking = bookingRepository.findById(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getStatus() != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot check-in. Current status: " + booking.getStatus());
        }

        booking.setStatus(1);

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

     @Override
     @Transactional
     public BookingResponse performCheckOut(UUID code) {
         BookingEntity booking = bookingRepository.findById(code)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

         if (booking.getStatus() != 1) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot check-out. Current status: " + booking.getStatus());
         }

         booking.setStatus(3);

         return bookingMapper.toDto(bookingRepository.save(booking));
     }

    @Override
    public List<BookingResponse> getBookingsByHotelId(UUID hotelId) {
        if (hotelId == null) {
            throw new IllegalArgumentException("Hotel ID nu poate fi null");
        }

        List<BookingEntity> bookings = bookingRepository.findByRoomRoomTypeHotelId(hotelId);

        return bookingMapper.toDtoList(bookings);
    }
}
