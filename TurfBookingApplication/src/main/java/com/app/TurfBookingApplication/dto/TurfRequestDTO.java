package com.app.TurfBookingApplication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // generates no-args constructor
@AllArgsConstructor // generates all-args constructor
@Builder // enables builder pattern
public class TurfRequestDTO {
	@NotBlank(message = "Turf Name is required")
	private String name;
	@NotBlank(message = "Turf Location is required")
	private String location;
	private Double pricePerHour;
	private Long ownerId; // reference to User (owner)
}
