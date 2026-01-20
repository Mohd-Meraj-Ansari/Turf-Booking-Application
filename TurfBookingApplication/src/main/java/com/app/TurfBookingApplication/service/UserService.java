package com.app.TurfBookingApplication.service;

import java.util.List;

import com.app.TurfBookingApplication.dto.TurfRequestDTO;
import com.app.TurfBookingApplication.dto.UpdateUserRequestDTO;
import com.app.TurfBookingApplication.dto.UserRequestDTO;
import com.app.TurfBookingApplication.dto.UserResponseDTO;
import com.app.TurfBookingApplication.entity.Turf;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO request);

    List<UserResponseDTO> getAllUsers();
    
    public UserResponseDTO updateMyProfile(
            UpdateUserRequestDTO request,
            String loggedInEmail);
    

	Turf addTurf(TurfRequestDTO dto);
    
}
