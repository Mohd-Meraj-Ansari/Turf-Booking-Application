package com.app.TurfBookingApplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.TurfBookingApplication.dto.AdminDashboardStatsDTO;
import com.app.TurfBookingApplication.service.AdminDashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping
    public ResponseEntity<AdminDashboardStatsDTO> getDashboardStats(
            Authentication authentication) {

        return ResponseEntity.ok(
                dashboardService.getDashboardStats(authentication)
        );
    }
}

