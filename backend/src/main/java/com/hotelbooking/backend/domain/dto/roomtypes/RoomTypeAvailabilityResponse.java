
package com.hotelbooking.backend.domain.dto.roomtypes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RoomTypeAvailabilityResponse {
    private UUID roomTypeId;
    private String roomName;
    private String roomFacilities;
    private Integer childCapacity;
    private Integer adultCapacity;
    private BigDecimal basePrice;

    /**
     * How many rooms of this type are available for the requested date interval.
     */
    private Long availableRooms;

	/**
	 * IDs of rooms that are available for the requested date interval.
	 */
	private List<UUID> availableRoomIds;

    private List<String> imageUrls;
}

