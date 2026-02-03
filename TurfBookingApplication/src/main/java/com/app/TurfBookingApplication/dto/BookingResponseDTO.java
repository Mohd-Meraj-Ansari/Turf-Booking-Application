package com.app.TurfBookingApplication.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.app.TurfBookingApplication.enums.BookingStatus;

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

	private Long bookingid;
	private Long turfId;
	private String turfName;
	private String clientName;
	private LocalDate bookingDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private Double totalAmount;
	private Double advanceAmount;
	private BookingStatus status;
	private LocalDate startDate;
	private LocalDate endDate;
	private Double discountAmount; 
}
