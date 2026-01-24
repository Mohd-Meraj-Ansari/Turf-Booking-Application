package com.app.TurfBookingApplication.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.TurfBookingApplication.dto.ClientDashboardStatsDTO;
import com.app.TurfBookingApplication.service.ClientDashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/client/dashboard")
@RequiredArgsConstructor
public class ClientDashboardController {

    private final ClientDashboardService dashboardService;

    @GetMapping("/stats")
    public ClientDashboardStatsDTO getDashboardStats(Authentication authentication) {
        return dashboardService.getDashboardStats(authentication);
    }
}
