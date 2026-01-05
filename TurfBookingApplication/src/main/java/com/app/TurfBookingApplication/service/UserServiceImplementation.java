package com.app.TurfBookingApplication.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {

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

