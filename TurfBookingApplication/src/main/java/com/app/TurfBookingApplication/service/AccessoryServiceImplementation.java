package com.app.TurfBookingApplication.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.app.TurfBookingApplication.dto.AccessoryRequestDTO;
import com.app.TurfBookingApplication.dto.AccessoryResponseDTO;
import com.app.TurfBookingApplication.entity.Accessory;
import com.app.TurfBookingApplication.entity.Turf;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.repository.AccessoryRepository;
import com.app.TurfBookingApplication.repository.TurfRepository;
import com.app.TurfBookingApplication.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AccessoryServiceImplementation implements AccessoryService {

	private final UserRepository userRepository;
	private final TurfRepository turfRepository;
	private final AccessoryRepository accessoryRepository;

	private Long getLoggedInUserId(Authentication authentication) {
		String email = authentication.getName();
		return userRepository.findByEmail(email).map(User::getId)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public AccessoryResponseDTO addAccessory(AccessoryRequestDTO request, Authentication authentication) {

		Long adminId = getLoggedInUserId(authentication);

		Turf turf = turfRepository.findByOwnerId(adminId).orElseThrow(() -> new RuntimeException("Owner has no turf"));

		Accessory accessory = Accessory.builder().accessoryName(request.getAccessoryName())
				.pricePerHour(request.getPricePerHour()).quantity(request.getQuantity()).turf(turf).build();

		Accessory saved = accessoryRepository.save(accessory);

		return AccessoryResponseDTO.builder().id(saved.getId()).accessoryName(saved.getAccessoryName())
				.pricePerHour(saved.getPricePerHour()).quantity(saved.getQuantity()).build();
	}

	@Override
	@Transactional
	public List<AccessoryResponseDTO> addMultipleAccessories(List<AccessoryRequestDTO> requests,
			Authentication authentication) {

		Long adminId = getLoggedInUserId(authentication);

		Turf turf = turfRepository.findByOwnerId(adminId).orElseThrow(() -> new RuntimeException("Owner has no turf"));

		List<Accessory> result = new ArrayList<>();

		for (AccessoryRequestDTO req : requests) {

			// skip empty rows
			if (req.getAccessoryName() == null || req.getAccessoryName().isBlank()) {
				continue;
			}

			Accessory accessory;

			if (req.getId() != null) {
				accessory = accessoryRepository.findById(req.getId())
						.orElseThrow(() -> new RuntimeException("Accessory not found"));

				accessory.setAccessoryName(req.getAccessoryName());
				accessory.setPricePerHour(req.getPricePerHour());
				accessory.setQuantity(req.getQuantity());

			} else {
				accessory = Accessory.builder().accessoryName(req.getAccessoryName())
						.pricePerHour(req.getPricePerHour()).quantity(req.getQuantity()).turf(turf).build();
			}

			result.add(accessoryRepository.save(accessory));
		}

		return result.stream().map(a -> AccessoryResponseDTO.builder().id(a.getId()).accessoryName(a.getAccessoryName())
				.pricePerHour(a.getPricePerHour()).quantity(a.getQuantity()).build()).toList();
	}

	@Override
	public List<AccessoryResponseDTO> getAccessoriesForOwner(Authentication authentication) {

		Long adminId = getLoggedInUserId(authentication);

		Turf turf = turfRepository.findByOwnerId(adminId).orElseThrow(() -> new RuntimeException("Owner has no turf"));

		return accessoryRepository.findByTurf(turf).stream()
				.map(a -> AccessoryResponseDTO.builder().id(a.getId()).accessoryName(a.getAccessoryName())
						.pricePerHour(a.getPricePerHour()).quantity(a.getQuantity()).build())
				.toList();
	}

	@Override
	public List<AccessoryResponseDTO> getAvailableAccessories() {

		return accessoryRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());

	}

	private AccessoryResponseDTO toDTO(Accessory accessory) {
		return new AccessoryResponseDTO(accessory.getId(), accessory.getAccessoryName(), accessory.getPricePerHour(),
				accessory.getQuantity());
	}
}
