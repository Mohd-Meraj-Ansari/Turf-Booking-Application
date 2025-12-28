package com.app.TurfBookingApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.TurfBookingApplication.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
    Role findByRoleName(String roleName);
}
