package com.app.TurfBookingApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BookingAccessoryResponseDTO {

    private Long id;
    private Long bookingId;
    private Long accessoryId;
    private String accessoryName; // optional: include accessory details
    private Integer hours;
    private Double cost; // calculated by server
}
