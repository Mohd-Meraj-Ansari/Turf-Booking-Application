package com.app.TurfBookingApplication.service;

import java.time.LocalDate;
import org.springframework.security.core.Authentication;
import com.app.TurfBookingApplication.dto.TurfAvailabilityViewDTO;

public interface TurfAvailabilityService {
    TurfAvailabilityViewDTO getAvailability(LocalDate date, Authentication authentication);
}