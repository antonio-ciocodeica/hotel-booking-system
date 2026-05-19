package com.hotelbooking.backend.controllers;

import com.hotelbooking.backend.domain.dto.UserDto;
import com.hotelbooking.backend.domain.dto.authentication.AuthResponse;
import com.hotelbooking.backend.domain.dto.authentication.LoginRequest;
import com.hotelbooking.backend.domain.dto.authentication.RegisterRequest;
import com.hotelbooking.backend.domain.entities.StaffEntity;
import com.hotelbooking.backend.domain.entities.UserEntity;
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

    // --- 1. REGISTER CLIENT NORMAL ---
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

        UserEntity savedUserEntity = userRepository.save(userEntity);
        UserDto userDto = userMapper.toDto(savedUserEntity);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    // --- 2. REGISTER STAFF (FĂRĂ HOTEL) ---
    @PostMapping(path = "/register/staff")
    public ResponseEntity<?> registerStaff(@RequestBody LoginRequest request) {
        if (staffRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Staff email already exists.");
        }

        StaffEntity staffEntity = StaffEntity.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(1) // 1 = STAFF
                .accountStatus(0) // 0 = PENDING
                .hotel(null) // <--- Setat pe null, conform cerinței
                .auditLogs(null)
                .build();

        staffRepository.save(staffEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created. Pending admin approval.");
    }

    // --- 3. LOGIN STAFF CU VERIFICARE PENDING ---
    @PostMapping(path = "/login/staff")
    public ResponseEntity<?> loginStaff(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails = authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        StaffEntity staff = staffRepository.findByEmail(loginRequest.getEmail()).orElse(null);

        if (staff == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Not a staff account.");
        }

        // Verificăm dacă e aprobat
        if (staff.getAccountStatus() == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Account is pending approval.");
        }

        String token = authenticationService.generateToken(userDetails);

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .expiresIn(86400)
                .build();

        return ResponseEntity.ok(authResponse);
    }

    // --- 4. LOGIN CLIENT NORMAL ---
    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails = authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        String token = authenticationService.generateToken(userDetails);

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .expiresIn(86400)
                .build();

        return ResponseEntity.ok(authResponse);
    }

    // --- 5. APROBARE CONT (DOAR PENTRU ADMIN) ---
    @PutMapping(path = "/staff/{id}/approve")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> approveStaffAccount(@PathVariable UUID id) {
        StaffEntity staff = staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

        staff.setAccountStatus(1); // Modificăm din PENDING(0) în ACTIVE(1)
        staffRepository.save(staff);

        return ResponseEntity.ok("Account approved successfully.");
    }
}