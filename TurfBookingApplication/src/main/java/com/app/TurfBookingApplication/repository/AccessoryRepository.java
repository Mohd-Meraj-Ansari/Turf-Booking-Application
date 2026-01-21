package com.app.TurfBookingApplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.TurfBookingApplication.entity.Accessory;
import com.app.TurfBookingApplication.entity.Turf;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {
    List<Accessory> findByTurf(Turf turf);
}

