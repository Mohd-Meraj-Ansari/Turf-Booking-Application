package com.app.TurfBookingApplication.dto;

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
public class BookingAccessoryRequestDTO {

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Accessory ID is required")
    private Long accessoryId;

    @NotNull(message = "Hours are required")
    @Positive(message = "Hours must be greater than 0")
    private Integer hours;
}
