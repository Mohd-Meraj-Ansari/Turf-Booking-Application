package com.app.TurfBookingApplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.TurfBookingApplication.dto.TurfResponseDTO;
import com.app.TurfBookingApplication.service.TurfService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/turf")
@RequiredArgsConstructor
public class TurfController {

    private final TurfService turfService;

    //get turf
    @GetMapping("/getturf")
    public TurfResponseDTO getTurf() {
        return turfService.getSingleTurf();
    }
}

