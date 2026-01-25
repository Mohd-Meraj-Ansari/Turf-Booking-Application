package com.app.TurfBookingApplication.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.TurfBookingApplication.dto.ProfileResponseDTO;
import com.app.TurfBookingApplication.dto.TurfRequestDTO;
import com.app.TurfBookingApplication.dto.UpdateUserRequestDTO;
import com.app.TurfBookingApplication.dto.UserRequestDTO;
import com.app.TurfBookingApplication.dto.UserResponseDTO;
import com.app.TurfBookingApplication.entity.Turf;
import com.app.TurfBookingApplication.entity.User;
import com.app.TurfBookingApplication.entity.Wallet;
import com.app.TurfBookingApplication.enums.UserRole;
import com.app.TurfBookingApplication.repository.TurfRepository;
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
	private final PasswordEncoder passwordEncoder;
	private final TurfRepository turfRepository;

	private static final Logger logger = Logger.getLogger(UserServiceImplementation.class);

	@Override
	public UserResponseDTO createUser(UserRequestDTO request) {

		logger.info("request received in service to create user"); // log into file
		if (userRepository.existsByEmail(request.getEmail())) { // check if email already exist in db
			throw new RuntimeException("Email already exists");
		}

		if (request.getRole() == UserRole.ADMIN) { // check if admin exist
			boolean adminExists = userRepository.existsByRole(UserRole.ADMIN);
			if (adminExists) {
				throw new RuntimeException("Admin already exists");
			}
		}

		User user = User.builder() // create user entity
				.name(request.getName()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())) // encode password
				.role(request.getRole()).build();

		User savedUser = userRepository.save(user); // save entity in database

		if (savedUser.getRole() == UserRole.CLIENT) { // if userRole is client, create wallet
			Wallet wallet = Wallet.builder()
					.client(savedUser)
					.balance(0.0) //default balance while new client is added
					.build();
			walletRepository.save(wallet); // save wallet with default balance
		}

		return UserResponseDTO.builder() // return response
				.id(savedUser.getId()).name(savedUser.getName()).email(savedUser.getEmail()).role(savedUser.getRole())
				.build();
	}

	@Override
	public List<UserResponseDTO> getAllUsers() { // return all users

		logger.info("request received in service to get all users"); // log into file
		return userRepository.findAll().stream().map(user -> UserResponseDTO.builder().id(user.getId())
				.name(user.getName()).email(user.getEmail()).role(user.getRole()).build()).toList();
	}

	@Override
    public UserResponseDTO updateMyProfile(
    		UserRequestDTO request,
            Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != UserRole.CLIENT) {
            throw new RuntimeException("Only clients can update profile");
        }

        // update name
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        // update password (encoded)
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
        }

        userRepository.save(user);

        return UserResponseDTO.builder()
        		.id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }


	
	private Long getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();  // from basic auth

        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Turf addTurf(TurfRequestDTO dto) {
    		logger.info("request received in service to add turf");

        Long adminId = getLoggedInUserId();  

        turfRepository.findById(adminId)
                .ifPresent(t -> { throw new RuntimeException("Admin already owns a turf"); });

        User owner = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Turf turf = Turf.builder()
                .turfName(dto.getTurfName())
                .location(dto.getLocation())
                .pricePerHour(dto.getPricePerHour())
                .owner(owner)
                .build();

        return turfRepository.save(turf);
    }


}
