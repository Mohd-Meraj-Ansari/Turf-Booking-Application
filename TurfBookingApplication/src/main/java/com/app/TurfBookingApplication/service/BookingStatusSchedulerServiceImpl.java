package com.app.TurfBookingApplication.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.TurfBookingApplication.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingStatusSchedulerServiceImpl
        implements BookingStatusSchedulerService {

    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    @Scheduled(cron = "0 * * * * *") // scheduler will run every minute
    public void markCompletedBookings() {
        bookingRepository.markCompletedBookings(
                LocalDate.now(),
                LocalTime.now()
        );
    }
}
