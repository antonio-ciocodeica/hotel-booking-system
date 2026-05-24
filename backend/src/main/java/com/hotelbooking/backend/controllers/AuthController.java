    package com.hotelbooking.backend.controllers;

    import com.hotelbooking.backend.domain.dto.UserDto;
    import com.hotelbooking.backend.domain.dto.authentication.AuthResponse;
    import com.hotelbooking.backend.domain.dto.authentication.LoginRequest;
    import com.hotelbooking.backend.domain.dto.authentication.RegisterRequest;
    import com.hotelbooking.backend.domain.entities.HotelEntity;
    import com.hotelbooking.backend.domain.entities.StaffEntity;
    import com.hotelbooking.backend.domain.entities.UserEntity;
    import com.hotelbooking.backend.repositories.HotelRepository;
    import com.hotelbooking.backend.mappers.UserMapper;
    import com.hotelbooking.backend.repositories.StaffRepository;
    import com.hotelbooking.backend.repositories.UserRepository;
    import com.hotelbooking.backend.security.AppRole;
    import com.hotelbooking.backend.services.AuthenticationService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDate;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.UUID;

    @CrossOrigin(origins = "http://localhost:5173")
    @RestController
    @RequestMapping(path = "/auth")
    @RequiredArgsConstructor
    public class AuthController {

        private final AuthenticationService authenticationService;
        private final PasswordEncoder passwordEncoder;
        private final UserRepository userRepository;
        private final StaffRepository staffRepository;
        private final UserMapper userMapper;
        private final HotelRepository hotelRepository;


        @PostMapping(path = "/register")
        public ResponseEntity<UserDto> register(@RequestBody RegisterRequest registerRequest) {
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            UserEntity userEntity = UserEntity.builder()
                    .name(registerRequest.getName())
                    .surname(registerRequest.getSurname())
                    .email(registerRequest.getEmail())
                    .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(AppRole.USER)
                    .phoneNumber(registerRequest.getPhoneNumber())
                    .accountCreationDate(LocalDate.now())
                    .build();
            return new ResponseEntity<>(userMapper.toDto(userRepository.save(userEntity)), HttpStatus.CREATED);
        }

        @PostMapping(path = "/register/staff")
        public ResponseEntity<?> registerStaff(@RequestBody LoginRequest request) {
            if (staffRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Staff email already exists.");
            }
            StaffEntity staffEntity = StaffEntity.builder()
                    .email(request.getEmail())
                    .passwordHash(passwordEncoder.encode(request.getPassword()))
                    .role(1)
                    .accountStatus(0)
                    .hotel(null)
                    .auditLogs(null)
                    .build();
            staffRepository.save(staffEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Account created. Pending admin approval.");
        }


        @PostMapping(path = "/login/staff")
        public ResponseEntity<?> loginStaff(@RequestBody LoginRequest loginRequest) {
            UserDetails userDetails = authenticationService.authenticate(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );

            StaffEntity staff = staffRepository.findByEmail(loginRequest.getEmail()).orElse(null);

            if (staff == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a staff or admin account.");
            }

            if (staff.getAccountStatus() == 0) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is pending approval.");
            }

            String token = authenticationService.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", staff.getRole());

            if (staff.getHotel() != null) {
                response.put("hotelId", staff.getHotel().getId());
            } else {
                response.put("hotelId", null);
            }

            return ResponseEntity.ok(response);
        }

        @PostMapping(path = "/login")
        public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
            UserDetails userDetails = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(AuthResponse.builder().token(authenticationService.generateToken(userDetails)).expiresIn(86400).build());
        }

        @GetMapping(path = "/staff/pending")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<StaffEntity>> getPendingStaff() {
            return ResponseEntity.ok(staffRepository.findByAccountStatus(0));
        }

        @PutMapping(path = "/staff/{id}/approve")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> approveStaffAccount(@PathVariable UUID id) {
            StaffEntity staff = staffRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Staff not found"));
            staff.setAccountStatus(1);
            staffRepository.save(staff);
            return ResponseEntity.ok("Account approved successfully.");
        }

        @DeleteMapping(path = "/staff/{id}/reject")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> rejectStaffAccount(@PathVariable UUID id) {
            StaffEntity staff = staffRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Staff not found"));
            if (staff.getAccountStatus() != 0) {
                throw new IllegalStateException("Only pending accounts can be rejected.");
            }
            staffRepository.delete(staff);
            return ResponseEntity.ok("Account rejected and removed.");
        }

        @GetMapping(path = "/staff")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<StaffEntity>> getAllStaff() {
            List<StaffEntity> allStaff = staffRepository.findAll();

            List<StaffEntity> onlyStaff = allStaff.stream()
                    .filter(s -> s.getRole() != null && s.getRole() == 1)
                    .toList();

            return ResponseEntity.ok(onlyStaff);
        }

        @PutMapping(path = "/staff/{id}/assign-hotel/{hotelId}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> assignHotelToStaff(@PathVariable UUID id, @PathVariable UUID hotelId) {
            StaffEntity staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

            HotelEntity hotel = hotelRepository.findById(hotelId)
                    .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

            staff.setHotel(hotel);

            staffRepository.save(staff);

            return ResponseEntity.ok("Hotel updated successfully.");
        }
    }