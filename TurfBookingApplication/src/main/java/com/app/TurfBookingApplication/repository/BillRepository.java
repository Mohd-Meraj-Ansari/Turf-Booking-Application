package com.app.TurfBookingApplication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.TurfBookingApplication.entity.Bill;
import com.app.TurfBookingApplication.entity.Booking;
import com.app.TurfBookingApplication.entity.User;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBooking(Booking booking);
    
 // Client bills
    @Query("""
        SELECT b FROM Bill b
        WHERE b.booking.client = :user
        ORDER BY b.billDate DESC
    """)
    List<Bill> findByUser(@Param("user") User user);

    @Query("""
        SELECT b FROM Bill b
        WHERE b.id = :billId
          AND b.booking.client = :user
    """)
    Optional<Bill> findByIdAndUser(
            @Param("billId") Long billId,
            @Param("user") User user
    );
}
