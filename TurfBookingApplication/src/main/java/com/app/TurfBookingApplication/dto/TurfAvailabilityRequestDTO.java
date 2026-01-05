package com.app.TurfBookingApplication.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TurfAvailabilityRequestDTO {

    @NotNull(message = "Turf ID is required")
    private Long turfId;

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Availability status is required")
    private Boolean available;

    @NotNull(message = "Open time is required")
    private LocalTime openTime;

    @NotNull(message = "Close time is required")
    private LocalTime closeTime;

    // Optional custom validation method
    @AssertTrue(message = "Close time must be after open time")
    public boolean isValidTimeRange() {
        if (openTime == null || closeTime == null) return true;
        return closeTime.isAfter(openTime);
    }
}
