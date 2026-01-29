package com.app.TurfBookingApplication.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.TurfBookingApplication.entity.Booking;
import com.app.TurfBookingApplication.entity.Turf;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.enums.BookingStatus;
import com.app.TurfBookingApplication.enums.BookingType;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	// HOURLY overlap
	@Query("""
			    SELECT COUNT(b) > 0
			    FROM Booking b
			    WHERE b.turf = :turf
			      AND b.bookingType = 'HOURLY'
			      AND b.status = 'BOOKED'
			      AND b.startDate = :date
			      AND (
			           b.startTime < :endTime
			           AND b.endTime > :startTime
			      )
			""")
	boolean existsHourlyOverlap(@Param("turf") Turf turf, @Param("date") LocalDate date,
			@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

	// FULL_DAY + MULTI_DAY overlap
	@Query("""
			    SELECT COUNT(b) > 0
			    FROM Booking b
			    WHERE b.turf = :turf
			      AND b.bookingType IN :types
			      AND b.status = 'BOOKED'
			      AND :date BETWEEN b.startDate AND b.endDate
			""")
	boolean existsNonHourlyOverlap(@Param("turf") Turf turf, @Param("date") LocalDate date,
			@Param("types") List<BookingType> types);

	// check any hourly booking on a date
	@Query("""
			    SELECT COUNT(b) > 0
			    FROM Booking b
			    WHERE b.turf = :turf
			      AND b.status = 'BOOKED'
			      AND b.bookingType = 'HOURLY'
			      AND b.startDate = :date
			""")
	boolean existsAnyHourlyOnDate(@Param("turf") Turf turf, @Param("date") LocalDate date);

	// booking history
	@Query("""
			    SELECT b FROM Booking b
			    WHERE b.client = :client
			    ORDER BY b.startDate DESC, b.startTime DESC
			""")
	List<Booking> findAllBookingsByClient(@Param("client") User client);

	// client dashboard stats
	long countByClient(User client);

	long countByClientAndStatus(User client, BookingStatus status);

	@Query("""
			    SELECT COUNT(b)
			    FROM Booking b
			    WHERE b.client = :client
			    AND b.status = 'BOOKED'
			    AND b.startDate >= :today
			""")
	long countUpcomingBookings(User client, LocalDate today);

	@Query("""
			    SELECT COALESCE(SUM(b.totalAmount), 0)
			    FROM Booking b
			    WHERE b.client = :client
			    AND b.status = 'BOOKED'
			""")
	double calculateTotalSpent(User client);

	// run scheduler for 'completion' of booking
	@Modifying
	@Query("""
			    UPDATE Booking b
			    SET b.status = 'COMPLETED'
			    WHERE b.status = 'BOOKED'
			      AND (
			           b.endDate < :today OR
			           (b.endDate = :today AND b.endTime < :now)
			      )
			""")
	void markCompletedBookings(LocalDate today, LocalTime now);

	// see all bookings
//    @Query("""
//    	    SELECT b
//    	    FROM Booking b
//    	    WHERE b.turf = :turf
//    	    ORDER BY b.startDate DESC, b.startTime DESC
//    	""")
//    	List<Booking> findAllByTurf(@Param("turf") Turf turf);

	List<Booking> findByTurfOrderByStartDateAscStartTimeAsc(Turf turf);

	// all bookings for admin
	@Query("""
			    SELECT b
			    FROM Booking b
			    WHERE b.turf = :turf
			    ORDER BY b.startDate ASC, b.startTime ASC
			""")
	List<Booking> findAllBookingsForTurf(@Param("turf") Turf turf);

	//find hourly booking
	@Query("""
			    SELECT b FROM Booking b
			    WHERE b.turf = :turf
			      AND b.bookingType = 'HOURLY'
			      AND b.status = 'BOOKED'
			      AND b.startDate = :date
			""")
	List<Booking> findHourlyBookings(@Param("turf") Turf turf, @Param("date") LocalDate date);

}
