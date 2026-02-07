package com.app.TurfBookingApplication.service;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.app.TurfBookingApplication.dto.ClientDashboardStatsDTO;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.entity.Wallet;
import com.app.TurfBookingApplication.enums.BookingStatus;
import com.app.TurfBookingApplication.enums.UserRole;
import com.app.TurfBookingApplication.repository.BookingRepository;
import com.app.TurfBookingApplication.repository.UserRepository;
import com.app.TurfBookingApplication.repository.WalletRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientDashboardServiceImpl implements ClientDashboardService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final WalletRepository walletRepository;

    @Override
    public ClientDashboardStatsDTO getDashboardStats(Authentication authentication) {

        User client = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (client.getRole() != UserRole.CLIENT) {
            throw new RuntimeException("Access denied");
        }

        long totalBookings =
                bookingRepository.countByClient(client);

        long cancelledBookings =
                bookingRepository.countByClientAndStatus(client, BookingStatus.CANCELLED);

        long completedBookings =
                bookingRepository.countByClientAndStatus(client, BookingStatus.COMPLETED);

        long upcomingBookings =
                bookingRepository.countUpcomingBookings(client, LocalDate.now());

//        double totalSpent =
                bookingRepository.calculateTotalSpent(client);
        
        double totalDiscount =
        	    bookingRepository.calculateTotalDiscount(client);
        
        double totalSpent = bookingRepository.calculateTotalSpent(client);

        Wallet wallet = walletRepository.findByClient(client)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        

        return ClientDashboardStatsDTO.builder()
                .totalBookings(totalBookings)
                .upcomingBookings(upcomingBookings)
                .completedBookings(completedBookings)
                .cancelledBookings(cancelledBookings)
                .walletBalance(wallet.getBalance())
                .totalSpent(totalSpent)
                .build();
    }
}
