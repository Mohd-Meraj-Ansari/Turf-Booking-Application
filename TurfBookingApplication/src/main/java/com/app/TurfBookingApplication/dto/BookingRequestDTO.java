package com.app.TurfBookingApplication.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.app.TurfBookingApplication.enums.BookingType;

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
public class BookingRequestDTO {
	
	@NotNull
    private Long turfId;

    @NotNull
    private BookingType bookingType;

    // HOURLY / FULL_DAY
    private LocalDate bookingDate;

    // HOURLY only
    private LocalTime startTime;
    private LocalTime endTime;

    // MULTI_DAY only
    private LocalDate startDate;
    private LocalDate endDate;

    // Accessories (optional)
    private List<BookingAccessoryRequestDTO> accessories;
}
