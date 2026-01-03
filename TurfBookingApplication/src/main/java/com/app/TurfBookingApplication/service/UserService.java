package com.app.TurfBookingApplication.service;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.TurfBookingApplication.controller.AppController;
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

}
