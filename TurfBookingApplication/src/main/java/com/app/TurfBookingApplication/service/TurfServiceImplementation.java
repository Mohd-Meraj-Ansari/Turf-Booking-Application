package com.app.TurfBookingApplication.service;


import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.TurfBookingApplication.dto.TurfAvailabilityRequestDTO;
import com.app.TurfBookingApplication.dto.TurfAvailabilityResponseDTO;
import com.app.TurfBookingApplication.entity.Turf;
import com.app.TurfBookingApplication.entity.TurfAvailability;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.repository.TurfAvailabilityRepository;
import com.app.TurfBookingApplication.repository.TurfRepository;
import com.app.TurfBookingApplication.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TurfServiceImplementation implements TurfService {

    private final UserRepository userRepository;
    private final TurfRepository turfRepository;
    private final TurfAvailabilityRepository turfAvailabilityRepository;

    private Long getLoggedInUserId(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<TurfAvailabilityResponseDTO> addAvailability(
            List<TurfAvailabilityRequestDTO> requests,
            Authentication authentication) {

        Long adminId = getLoggedInUserId(authentication);

        Turf turf = turfRepository.findByOwnerId(adminId)
                .orElseThrow(() -> new RuntimeException("Admin does not own any turf"));

        List<TurfAvailability> savedAvailability = requests.stream()
                .map(req -> TurfAvailability.builder()
                        .turf(turf)
                        .dayOfWeek(req.getDayOfWeek())
                        .available(req.getAvailable())
                        .openTime(req.getOpenTime())
                        .closeTime(req.getCloseTime())
                        .build())
                .map(turfAvailabilityRepository::save)
                .toList();

        return savedAvailability.stream()
                .map(a -> TurfAvailabilityResponseDTO.builder()
                        .id(a.getId())
                        .turfId(a.getTurf().getId())              
                        .turfName(a.getTurf().getTurfName())      
                        .dayOfWeek(a.getDayOfWeek())
                        .available(a.isAvailable())
                        .openTime(a.getOpenTime())
                        .closeTime(a.getCloseTime())
                        .build())
                .toList();
    }
}

