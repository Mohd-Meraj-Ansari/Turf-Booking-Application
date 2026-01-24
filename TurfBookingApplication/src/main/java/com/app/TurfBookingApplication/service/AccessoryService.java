package com.app.TurfBookingApplication.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.app.TurfBookingApplication.dto.AccessoryRequestDTO;
import com.app.TurfBookingApplication.dto.AccessoryResponseDTO;

public interface AccessoryService {

    AccessoryResponseDTO addAccessory(AccessoryRequestDTO request, Authentication authentication);

    List<AccessoryResponseDTO> getAccessoriesForOwner(Authentication authentication);

	List<AccessoryResponseDTO> addMultipleAccessories(List<AccessoryRequestDTO> requests,
			Authentication authentication);
	
	List<AccessoryResponseDTO> getAvailableAccessories();
}

