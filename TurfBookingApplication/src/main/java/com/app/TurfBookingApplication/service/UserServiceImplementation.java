package com.app.TurfBookingApplication.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
			Wallet wallet = Wallet.builder().client(savedUser).balance(0.0).build();
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
	public UserResponseDTO updateMyProfile(UpdateUserRequestDTO request, String loggedInEmail) {

		logger.info("request received in service to update user details");

		User user = userRepository.findByEmail(loggedInEmail) // fetch logged-in user form db
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (request.getName() != null) { // check and update username
			user.setName(request.getName());
		}

		if (request.getPassword() != null) { // check and update password
			user.setPassword(passwordEncoder.encode(request.getPassword()));
		}

		User updatedUser = userRepository.save(user); // save new details in db

		return UserResponseDTO.builder() // return response
				.id(updatedUser.getId()).name(updatedUser.getName()).email(updatedUser.getEmail())
				.role(updatedUser.getRole()).build();
	}

	@Transactional //multiple operations in db
	@Override
	public Turf addTurf(TurfRequestDTO dto , Long adminId) {

		turfRepository.findByAdminId(adminId).ifPresent(t -> {      //if admin already has one turf
			throw new RuntimeException("Admin already owns a turf");
		});

		User owner = userRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found")); // if admin does not exist

		Turf turf = Turf.builder()  // if not then create turf entity
				.turfName(dto.getTurfName())
				.turfType(dto.getTurfType())
				.location(dto.getLocation())
				.pricePerHour(dto.getPricePerHour())
				.owner(owner)
				.build();

		return turfRepository.save(turf);  //return turf
	}

}
