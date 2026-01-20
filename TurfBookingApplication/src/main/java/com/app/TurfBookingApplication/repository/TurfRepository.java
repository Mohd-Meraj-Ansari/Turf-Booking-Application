package com.app.TurfBookingApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.TurfBookingApplication.entity.Turf;

public interface TurfRepository extends JpaRepository<Turf, Long> {

//    Optional<Turf> findByAdminId(Long adminId);
}
