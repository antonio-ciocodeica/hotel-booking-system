package com.hotelbooking.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * File upload configuration.
 *
 * baseDir is a folder on disk where uploaded files are stored.
 * These files are exposed via {@code /uploads/**}.
 */
@ConfigurationProperties(prefix = "app.uploads")
public record UploadsConfig(
        String baseDir
) {
}

