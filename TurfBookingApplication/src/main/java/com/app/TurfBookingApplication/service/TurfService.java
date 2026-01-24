package com.app.TurfBookingApplication.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.app.TurfBookingApplication.dto.TurfAvailabilityRequestDTO;
import com.app.TurfBookingApplication.dto.TurfAvailabilityResponseDTO;
import com.app.TurfBookingApplication.dto.TurfResponseDTO;

public interface TurfService {

	List<TurfAvailabilityResponseDTO> addAvailability(List<TurfAvailabilityRequestDTO> request,
			Authentication authentication);

	TurfResponseDTO getSingleTurf();
}
