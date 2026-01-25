package com.app.TurfBookingApplication.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponseDTO {
	private String name;
	private String password;

}
