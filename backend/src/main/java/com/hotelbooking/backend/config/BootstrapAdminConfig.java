package com.hotelbooking.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Optional dev-time bootstrap of a global ADMIN account.
 *
 * Configure via application.yml / environment variables, e.g.:
 *   app.bootstrap-admin.enabled=true
 *   app.bootstrap-admin.email=admin@example.com
 *   app.bootstrap-admin.password=change-me
 */
@ConfigurationProperties(prefix = "app.bootstrap-admin")
public record BootstrapAdminConfig(
        boolean enabled,
        String email,
        String password
) {
}


