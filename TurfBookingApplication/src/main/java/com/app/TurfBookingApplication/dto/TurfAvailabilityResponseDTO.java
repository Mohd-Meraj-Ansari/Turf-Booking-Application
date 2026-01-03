package com.app.TurfBookingApplication.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TurfAvailabilityResponseDTO {

    private Long id;
    private Long turfId;
    private String turfName;   // optional for client convenience
    private DayOfWeek dayOfWeek;
    private Boolean available;
    private LocalTime openTime;
    private LocalTime closeTime;
}
