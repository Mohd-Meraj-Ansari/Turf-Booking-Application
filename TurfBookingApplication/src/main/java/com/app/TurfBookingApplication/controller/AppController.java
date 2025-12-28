package com.app.TurfBookingApplication.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.TurfBookingApplication.dto.UserRequestDTO;
import com.app.TurfBookingApplication.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class AppController {
	@Autowired
	private UserService userService;
	
	private static final Logger logger = Logger.getLogger(AppController.class);

	@GetMapping("/test")
	public String test() {
		logger.info("request recieved in controller for test endpoint");
		return "test endpoint";
	}

	@PostMapping("/add-user")
	public ResponseEntity<String> addUser(@RequestBody UserRequestDTO dto) {
		logger.info("request recieved in controller for add user request");
		userService.addUser(dto);
		return new ResponseEntity<String>("User registered successfully", HttpStatus.OK);
	}
}
