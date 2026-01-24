package com.app.TurfBookingApplication.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.TurfBookingApplication.entity.Booking;
import com.app.TurfBookingApplication.entity.Turf;
import com.app.TurfBookingApplication.enums.BookingType;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // ✅ HOURLY overlap (already correct)
    @Query("""
        SELECT COUNT(b) > 0
        FROM Booking b
        WHERE b.turf = :turf
          AND b.bookingType = 'HOURLY'
          AND b.startDate = :date
          AND (
                (:start < b.endTime AND :end > b.startTime)
              )
    """)
    boolean existsHourlyOverlap(
            @Param("turf") Turf turf,
            @Param("date") LocalDate date,
            @Param("start") LocalTime startTime,
            @Param("end") LocalTime endTime);

    // ✅ FIXED: FULL_DAY + MULTI_DAY overlap
    @Query("""
        SELECT COUNT(b) > 0
        FROM Booking b
        WHERE b.turf = :turf
          AND b.bookingType IN :types
          AND :date BETWEEN b.startDate AND b.endDate
    """)
    boolean existsNonHourlyOverlap(
            @Param("turf") Turf turf,
            @Param("date") LocalDate date,
            @Param("types") List<BookingType> types);
}
