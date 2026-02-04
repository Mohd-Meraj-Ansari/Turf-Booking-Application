package com.app.TurfBookingApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.TurfBookingApplication.entity.Bill;
import com.app.TurfBookingApplication.entity.Booking;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBooking(Booking booking);
}
