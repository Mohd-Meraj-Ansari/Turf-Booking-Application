package com.app.TurfBookingApplication.dto;

import com.app.TurfBookingApplication.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UserRequestDTO {

	@NotBlank(message = "Name is required")
	@Size(min = 3, max = 30, message = "Title must be between 3 and 30 characters")
	    private String name;

	    @Email
	    @NotBlank
	    private String email;

	    @NotBlank(message = "Password is required")
	    private String password;
	    
	    private UserRole role;
}
