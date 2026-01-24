package com.app.TurfBookingApplication.service;

import org.springframework.security.core.Authentication;

import com.app.TurfBookingApplication.dto.ClientDashboardStatsDTO;

public interface ClientDashboardService {

    ClientDashboardStatsDTO getDashboardStats(Authentication authentication);
}
