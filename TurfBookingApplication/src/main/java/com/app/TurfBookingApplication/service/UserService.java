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

	private static final Logger logger = Logger.getLogger(AppController.class); // logger is created

	private final UserRepository userRepository; // reference is created for userRepository
	private final RoleRepository roleRepository; // reference is created for roleRepository
	private final PasswordEncoder passwordEncoder; // // reference is created for passwordEncoder

	public User addUser(UserRequestDTO dto) { 

		logger.info("request recieved in service for add user request");  // logs into log file

		if (userRepository.existsByEmail(dto.getEmail())) {   //check if email already exist in db
			throw new RuntimeException("Email already exists"); // if yes throw runtime exception
		}

		Role userRole = roleRepository.findByRoleName("ROLE_USER");  //fetching role from db
		Set<Role> roles = new HashSet<>();                           // creating hashset for roles
		roles.add(userRole);                                         // adding user role to roles 

		User user = User.builder()  // creating user entity
				.name(dto.getName())
				.email(dto.getEmail())  
				.password(passwordEncoder.encode(dto.getPassword()))
				.roles(roles)
				.build();

		return userRepository.save(user);       // adding entity to db
	}
}
