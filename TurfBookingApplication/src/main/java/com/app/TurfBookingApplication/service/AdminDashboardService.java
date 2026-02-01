package com.app.TurfBookingApplication.service;

import org.springframework.security.core.Authentication;

import com.app.TurfBookingApplication.dto.AdminDashboardStatsDTO;

public interface AdminDashboardService {
    AdminDashboardStatsDTO getDashboardStats(Authentication authentication);
}