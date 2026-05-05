package com.hotelbooking.backend.services;

import com.hotelbooking.backend.domain.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    UserDetails authenticate(String username, String password);
    String generateToken(UserDetails userDetails);
    UserDetails validateToken(String token);
    UserEntity getUserEntityFromAuth();
}
