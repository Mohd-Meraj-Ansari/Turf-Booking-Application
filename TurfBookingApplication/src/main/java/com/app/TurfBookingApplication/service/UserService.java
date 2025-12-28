package com.app.TurfBookingApplication.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.TurfBookingApplication.controller.AppController;
import com.app.TurfBookingApplication.dto.UserRequestDTO;
import com.app.TurfBookingApplication.entity.Role;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.repository.RoleRepository;
import com.app.TurfBookingApplication.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private static final Logger logger = Logger.getLogger(AppController.class);

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public User addUser(UserRequestDTO dto) {

		logger.info("request recieved in service for add user request");
		
		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		Role userRole = roleRepository.findByRoleName("ROLE_USER");
		  Set<Role> roles = new HashSet<>();
	        roles.add(userRole);

		User user = User
				.builder()
				.name(dto
						.getName())
				.email(dto
						.getEmail())
				.password(passwordEncoder
						.encode(dto
								.getPassword()))
				.roles(roles)
				.build();

		return userRepository.save(user);
	}
}
