package com.app.TurfBookingApplication.dto;

import java.time.LocalDate;

import com.app.TurfBookingApplication.enums.AdvertisementDuration;
import com.app.TurfBookingApplication.enums.AdvertisementStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GroundAdvertisementResponseDTO {

    private Long id;
    private Long advertiserId;
    private String advertiserName; // optional for client convenience
    private Long turfId;
    private String turfName;       // optional for client convenience
    private AdvertisementDuration duration;
    private AdvertisementStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
}
