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
public class BookingRequestDTO {

	private LocalDateTime bookingDate; // when the booking is scheduled
	private Long userId; // reference to User
	private Long turfId; // reference to Turf
}
