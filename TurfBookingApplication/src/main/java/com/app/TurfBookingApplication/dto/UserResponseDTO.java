package com.app.TurfBookingApplication.dto;

import com.app.TurfBookingApplication.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

	private Long id;
	private String name;
	private String email;
	private UserRole role;
}
