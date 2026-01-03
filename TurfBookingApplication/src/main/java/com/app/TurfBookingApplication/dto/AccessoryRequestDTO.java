package com.app.TurfBookingApplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AccessoryRequestDTO {

    @NotBlank(message = "Accessory name is required")
    private String accessoryName;

    @NotNull(message = "Price per hour is required")
    @Positive(message = "Price per hour must be greater than 0")
    private Double pricePerHour;

    @NotNull(message = "Turf ID is required")
    private Long turfId;
}
