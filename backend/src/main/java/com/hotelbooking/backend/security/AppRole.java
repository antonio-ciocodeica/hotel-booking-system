package com.hotelbooking.backend.security;

/**
 * Application roles.
 *
 * Note: Spring Security conventions expect role authorities to be prefixed with "ROLE_".
 * Use {@link #asAuthority()} when creating {@link org.springframework.security.core.GrantedAuthority}.
 */
public enum AppRole {
    USER,
    STAFF,
    ADMIN;

    public String asAuthority() {
        return "ROLE_" + name();
    }
}

