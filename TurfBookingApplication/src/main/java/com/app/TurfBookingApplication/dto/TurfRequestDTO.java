package com.app.TurfBookingApplication.dto;

import com.app.TurfBookingApplication.enums.TurfType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurfRequestDTO {
	 @NotBlank(message = "Turf name is required")
	    private String turfName;

	    @NotNull(message = "Turf type is required")
	    private TurfType turfType;

	    @NotBlank(message = "Location is required")
	    private String location;

	    @NotNull(message = "Price per hour is required")
	    @Positive(message = "Price per hour must be greater than zero")
	    private Double pricePerHour;

	    @NotNull(message = "Owner (admin) ID is required")
	    private Long ownerId; // Admin creating the turf
}
