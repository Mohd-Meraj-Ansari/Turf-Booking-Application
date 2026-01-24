package com.app.TurfBookingApplication.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.TurfBookingApplication.dto.BookingRequestDTO;
import com.app.TurfBookingApplication.dto.BookingResponseDTO;
import com.app.TurfBookingApplication.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

	private static final Logger logger = Logger.getLogger(BookingController.class);

	private final BookingService bookingService;

	@PostMapping("/book")
	public ResponseEntity<BookingResponseDTO> bookTurf(@Validated @RequestBody BookingRequestDTO request,
			Authentication authentication) {

		logger.info("Request received to book turf");

		BookingResponseDTO response = bookingService.bookTurf(request, authentication);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
