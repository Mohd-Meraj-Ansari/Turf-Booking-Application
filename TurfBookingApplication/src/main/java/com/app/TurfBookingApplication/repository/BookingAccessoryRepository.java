package com.app.TurfBookingApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.TurfBookingApplication.entity.BookingAccessory;

@Repository
public interface BookingAccessoryRepository
        extends JpaRepository<BookingAccessory, Long> {
}