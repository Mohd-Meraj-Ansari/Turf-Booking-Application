package com.app.TurfBookingApplication.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.app.TurfBookingApplication.controller.AppController;
import com.app.TurfBookingApplication.dto.UserRequestDTO;
import com.app.TurfBookingApplication.dto.UserResponseDTO;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.entity.Wallet;
import com.app.TurfBookingApplication.enums.UserRole;
import com.app.TurfBookingApplication.repository.UserRepository;
import com.app.TurfBookingApplication.repository.WalletRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    
    private static final Logger logger=Logger.getLogger(UserServiceImplementation.class);

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
    	
    		logger.info("request received in service to create user"); // log into file  
        if (userRepository.existsByEmail(request.getEmail())) {  // check if email already exist in db
            throw new RuntimeException("Email already exists");
        }

        if (request.getRole() == UserRole.ADMIN) {   // check if admin exist
            boolean adminExists = userRepository.existsByRole(UserRole.ADMIN);
            if (adminExists) {
                throw new RuntimeException("Admin already exists");
            }
        }

        	
        User user = User.builder()			// create user entity
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword()) // encode later
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);	// save entity in database


        if (savedUser.getRole() == UserRole.CLIENT) {	//if userRole is client, create wallet 
            Wallet wallet = Wallet.builder()
                    .client(savedUser)
                    .balance(0.0)
                    .build();
            walletRepository.save(wallet);				// save wallet with default balance 
        }

        
        return UserResponseDTO.builder()                 // return response 
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    @Override
    public List<UserResponseDTO> getAllUsers() { 		// return all users 

    	logger.info("request received in service to get all users");  // log into file
        return userRepository.findAll()
                .stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build())
                .toList();
    }
}

