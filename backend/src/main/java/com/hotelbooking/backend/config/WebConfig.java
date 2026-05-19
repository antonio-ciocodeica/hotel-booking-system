package com.hotelbooking.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UploadsConfig uploadsConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseDir = uploadsConfig.baseDir() == null || uploadsConfig.baseDir().isBlank()
                ? "uploads"
                : uploadsConfig.baseDir();

        Path uploadPath = Paths.get(baseDir).toAbsolutePath().normalize();

        // Expose local uploaded files under /uploads/**
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}

