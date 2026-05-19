package com.hotelbooking.backend.domain.dto.hotels;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {

	@NotBlank
	private String name;

	@NotBlank
	private String location;

	@NotBlank
	private String facilities;

	private String description;
}


