package com.hotelbooking.backend.controllers;

import com.hotelbooking.backend.domain.dto.UserDto;
import com.hotelbooking.backend.domain.dto.authentication.AuthResponse;
import com.hotelbooking.backend.domain.dto.authentication.LoginRequest;
import com.hotelbooking.backend.domain.dto.authentication.RegisterRequest;
import com.hotelbooking.backend.domain.entities.UserEntity;
import com.hotelbooking.backend.mappers.UserMapper;
import com.hotelbooking.backend.repositories.UserRepository;
import com.hotelbooking.backend.security.AppRole;
import com.hotelbooking.backend.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
}
