package com.app.TurfBookingApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.enums.UserRole;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByRole(UserRole role);
}

