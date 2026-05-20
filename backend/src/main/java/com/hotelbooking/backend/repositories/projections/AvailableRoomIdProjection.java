package com.hotelbooking.backend.repositories.projections;

import java.util.UUID;

/**
 * Projection for available room IDs grouped by room type.
 */
public interface AvailableRoomIdProjection {
    UUID getRoomTypeId();

    UUID getRoomId();
}

