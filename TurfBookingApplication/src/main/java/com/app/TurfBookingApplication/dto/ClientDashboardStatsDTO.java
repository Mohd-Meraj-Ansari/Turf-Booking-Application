package com.app.TurfBookingApplication.dto;

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
public class ClientDashboardStatsDTO {

    private long totalBookings;
    private long upcomingBookings;
    private long completedBookings;
    private long cancelledBookings;

    private double walletBalance;
    private double totalSpent;
}
