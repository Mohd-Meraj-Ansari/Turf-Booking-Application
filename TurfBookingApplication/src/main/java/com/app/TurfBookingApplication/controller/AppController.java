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

	@PostMapping("/add-user")             //endpoint to add new user 
	public ResponseEntity<String> addUser(@RequestBody UserRequestDTO dto) { 
		logger.info("request recieved in controller for add user request"); // log recieved request into file 
		userService.addUser(dto);  // call adduser of service class and pass recieved dto as parameter
		return new ResponseEntity<String>("User registered successfully", HttpStatus.OK);  // if user is added return with message and OK response 
	}
}
