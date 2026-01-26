package com.app.TurfBookingApplication.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.TurfBookingApplication.dto.TurfAvailabilityRequestDTO;
import com.app.TurfBookingApplication.dto.TurfAvailabilityResponseDTO;
import com.app.TurfBookingApplication.dto.TurfRequestDTO;
import com.app.TurfBookingApplication.dto.TurfResponseDTO;
import com.app.TurfBookingApplication.dto.UserRequestDTO;
import com.app.TurfBookingApplication.dto.UserResponseDTO;
import com.app.TurfBookingApplication.entity.Turf;
import com.app.TurfBookingApplication.service.TurfService;
import com.app.TurfBookingApplication.service.UserService;

@RestController
@RequestMapping("/api/users")
public class AppController {
	private final UserService userService;
	private final TurfService turfService;

	public AppController(UserService userService, TurfService turfService) {
	    this.userService = userService;
	    this.turfService = turfService;
	}

	private static final Logger logger = Logger.getLogger(AppController.class); // create logger for this class

	@PostMapping("/register") // register endpoint to register users
	public ResponseEntity<UserResponseDTO> registerUser( // accepts request dto and returns response dto
			@RequestBody UserRequestDTO request) {

		logger.info("request received in controller to create user"); // log request
		UserResponseDTO response = userService.createUser(request); // calls createUser in service class
		return ResponseEntity.status(HttpStatus.CREATED).body(response); // returns response
	}

	@GetMapping("/all") // all endpoint to retrive all users
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() { // returns list of user response dto
		logger.info("request received in controller to get all users"); // log request
		List<UserResponseDTO> users = userService.getAllUsers(); // calls getAllusers of userService class
		return ResponseEntity.ok(users); // returns list of users
	}

	 @PutMapping("/update-my-profile")
	    public ResponseEntity<UserResponseDTO> updateMyProfile(
	            @RequestBody UserRequestDTO request,
	            Authentication authentication) {
		 logger.info("request received in controller to update profile");
		 
	        UserResponseDTO response =
	                userService.updateMyProfile(request, authentication);

	        return ResponseEntity.ok(response);
	    }

	@PostMapping("/add-turf") // endpoint to add turf
	public ResponseEntity<TurfResponseDTO> addTurf(@Validated @RequestBody TurfRequestDTO dto) {

		logger.info("request received in controller to add turf");
		Turf turf = userService.addTurf(dto); // call addturf in userservice

		TurfResponseDTO response = TurfResponseDTO.builder() // turf into turfResponseDTO
				.id(turf.getId())
				.turfName(turf.getTurfName())
				.location(turf.getLocation())
				.pricePerHour(turf.getPricePerHour())
				.ownerId(turf.getOwner().getId())
				.build();

		return ResponseEntity.ok(response); // return dto
	}
	
	@PostMapping("/availability/add")
	public ResponseEntity<List<TurfAvailabilityResponseDTO>> addAvailability(
	        @RequestBody List<TurfAvailabilityRequestDTO> request,
	        Authentication authentication) {

	    logger.info("request received in controller to add turf availability");
	    
	    return ResponseEntity.ok(turfService.addAvailability(request, authentication));
	}


	
}
