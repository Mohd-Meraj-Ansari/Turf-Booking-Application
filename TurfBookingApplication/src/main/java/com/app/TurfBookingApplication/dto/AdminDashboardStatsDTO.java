package com.app.TurfBookingApplication.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminDashboardStatsDTO {

    private long todaysBookings;
    private double todaysEarnings;
    private long upcomingBookings;
    private long cancelledBookings;
    private long totalBookings;
    private long totalClients;
}

