package com.hotelbooking.backend.security;

public enum AppRole {
    USER,
    STAFF,
    ADMIN;

    public String asAuthority() {
        return "ROLE_" + name();
    }
}

