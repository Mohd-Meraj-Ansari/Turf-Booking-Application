package com.app.TurfBookingApplication.controller;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.app.TurfBookingApplication.dto.TurfAvailabilityViewDTO;
import com.app.TurfBookingApplication.service.TurfAvailabilityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/turf/allavailability")
@RequiredArgsConstructor
public class TurfAvailabilityController {

    private final TurfAvailabilityService availabilityService;

    @GetMapping
    public TurfAvailabilityViewDTO getAvailability(
            @RequestParam LocalDate date,
            Authentication authentication) {

        return availabilityService.getAvailability(date, authentication);
    }
}

