package com.app.TurfBookingApplication.dto;

import java.time.LocalDateTime;

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
public class BookingResponseDTO {

	private Long id;
	private LocalDateTime bookingDate;

	private Long userId;
	private String userName; 
	private Long turfId;
	private String turfName; 
}
