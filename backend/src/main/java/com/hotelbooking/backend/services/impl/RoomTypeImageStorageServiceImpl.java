package com.hotelbooking.backend.services.impl;

import com.hotelbooking.backend.config.UploadsConfig;
import com.hotelbooking.backend.services.RoomTypeImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomTypeImageStorageServiceImpl implements RoomTypeImageStorageService {

    private final UploadsConfig uploadsConfig;

    @Override
    public String storeRoomTypeImage(UUID roomTypeId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty file");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only image files are allowed");
        }

        String baseDir = uploadsConfig.baseDir() == null || uploadsConfig.baseDir().isBlank()
                ? "uploads"
                : uploadsConfig.baseDir();

        String originalName = Objects.toString(file.getOriginalFilename(), "image");
        String ext = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0 && dot < originalName.length() - 1) {
            ext = originalName.substring(dot);
        }

        String safeFileName = UUID.randomUUID() + ext;

        Path directory = Paths.get(baseDir, "room-types", roomTypeId.toString())
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(directory);
            Path target = directory.resolve(safeFileName).normalize();

            // Ensure the target path is still within the intended directory (basic traversal protection)
            if (!target.startsWith(directory)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file name");
            }

            file.transferTo(target);
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store image");
        }

        return "/uploads/room-types/" + roomTypeId + "/" + safeFileName;
    }
}

