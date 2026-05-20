package com.hotelbooking.backend.repositories.projections;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Projection for hotel availability per room type.
 * Used by {@code RoomTypeRepository.findRoomTypeAvailabilityByHotel}.
 */
public interface RoomTypeAvailabilityProjection {
    UUID getRoomTypeId();

    String getRoomName();

    String getRoomFacilities();

    Integer getChildCapacity();

    Integer getAdultCapacity();

    BigDecimal getBasePrice();

    Long getAvailableRooms();
}

