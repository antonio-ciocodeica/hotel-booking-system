package com.hotelbooking.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.uploads")
public record UploadsConfig(
        String baseDir
) {
}
