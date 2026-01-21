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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurfAvailabilityRequestDTO {

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Availability status is required")
    private Boolean available;

    private LocalTime openTime;

    private LocalTime closeTime;

    @AssertTrue(message = "Open and close time are required if available is true")
    public boolean isTimeRequiredWhenAvailable() {
        if (Boolean.TRUE.equals(available)) {
            return openTime != null && closeTime != null;
        }
        return true; // no time required when unavailable
    }

    @AssertTrue(message = "Close time must be after open time")
    public boolean isValidTimeRange() {
        if (Boolean.TRUE.equals(available) && openTime != null && closeTime != null) {
            return closeTime.isAfter(openTime);
        }
        return true;
    }
}
