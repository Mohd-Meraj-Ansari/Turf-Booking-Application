package com.app.TurfBookingApplication.dto;

import java.util.List;

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
public class BookingBreakdownDTO {

    // Turf info
    private Long turfId;
    private String turfName;

    // Booking info
    private String bookingType; // HOURLY, FULL_DAY, MULTI_DAY
    private Integer totalHours; // for hourly
    private Integer totalDays;  // for full/multi day

    // Pricing
    private Double basePrice;
    private Double turfCost;

    // Accessories
    private List<AccessoryCostDTO> accessories;
    private Double accessoriesTotal;

    // Final amounts
    private Double totalAmount;
    private Double advanceAmount;
    private Double remainingAmount;
}
