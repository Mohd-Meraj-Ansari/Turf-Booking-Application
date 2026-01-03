package com.app.TurfBookingApplication.dto;

import java.time.LocalDate;

import com.app.TurfBookingApplication.enums.AdvertisementDuration;
import com.app.TurfBookingApplication.enums.AdvertisementStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GroundAdvertisementRequestDTO {

    @NotNull(message = "Advertiser ID is required")
    private Long advertiserId;

    @NotNull(message = "Turf ID is required")
    private Long turfId;

    @NotNull(message = "Advertisement duration is required")
    private AdvertisementDuration duration;

    @NotNull(message = "Advertisement status is required")
    private AdvertisementStatus status;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be today or in the future")
    private LocalDate endDate;
}
