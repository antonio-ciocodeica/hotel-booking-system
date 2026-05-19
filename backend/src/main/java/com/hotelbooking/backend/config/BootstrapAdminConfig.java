package com.hotelbooking.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.bootstrap-admin")
public record BootstrapAdminConfig(
        boolean enabled,
        String email,
        String password
) {
}
