package com.app.TurfBookingApplication.service;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.app.TurfBookingApplication.dto.AdminDashboardStatsDTO;
import com.app.TurfBookingApplication.entity.Turf;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.enums.UserRole;
import com.app.TurfBookingApplication.repository.BookingRepository;
import com.app.TurfBookingApplication.repository.TurfRepository;
import com.app.TurfBookingApplication.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;
    private final TurfRepository turfRepository;
    private final BookingRepository bookingRepository;

    @Override
    public AdminDashboardStatsDTO getDashboardStats(Authentication authentication) {

        User admin = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Access denied");
        }

        Turf turf = turfRepository.findByOwnerId(admin.getId())
                .orElseThrow(() -> new RuntimeException("Admin has no turf"));

        LocalDate today = LocalDate.now();

        return AdminDashboardStatsDTO.builder()
                .todaysBookings(
                        bookingRepository.countTodayBookings(turf, today))
                .todaysEarnings(
                        bookingRepository.sumTodayEarnings(turf, today))
                .upcomingBookings(
                        bookingRepository.countUpcomingBookings(turf, today))
                .cancelledBookings(
                        bookingRepository.countCancelledBookings(turf))
                .totalBookings(
                        bookingRepository.countByTurf(turf))
                .totalClients(
                        userRepository.countTotalClients())
                .build();
    }
}
