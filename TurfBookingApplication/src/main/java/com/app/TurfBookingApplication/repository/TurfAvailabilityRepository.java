package com.app.TurfBookingApplication.repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.TurfBookingApplication.entity.Turf;
import com.app.TurfBookingApplication.entity.TurfAvailability;

public interface TurfAvailabilityRepository extends JpaRepository<TurfAvailability, Long> {

	List<TurfAvailability> findByTurf(Turf turf);

	Optional<TurfAvailability> findByTurfAndDayOfWeek(Turf turf, DayOfWeek day);
}
