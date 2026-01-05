package com.app.TurfBookingApplication.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.TurfBookingApplication.dto.UserRequestDTO;
import com.app.TurfBookingApplication.dto.UserResponseDTO;
import com.app.TurfBookingApplication.service.UserService;


@RestController
@RequestMapping("/api/users")
public class AppController {
	private final UserService userService;

    public AppController(UserService userService) {
        this.userService = userService;
    }

	    @PostMapping("/register")       // register endpoint to register users
	    public ResponseEntity<UserResponseDTO> registerUser(      // accepts request dto and returns response dto
	            @RequestBody UserRequestDTO request) {

	        UserResponseDTO response = userService.createUser(request);   // calls createUser in service class
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);  // returns response
	    }

	    @GetMapping("/all")  // all endpoint to retrive all users
	    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {  // returns list of user response dto 

	        List<UserResponseDTO> users = userService.getAllUsers();   // calls getAllusers of userService class
	        return ResponseEntity.ok(users); // returns list of users
	    }

}
