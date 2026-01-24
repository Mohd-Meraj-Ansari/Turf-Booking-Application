package com.app.TurfBookingApplication.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.TurfBookingApplication.dto.BookingBreakdownDTO;
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
	
	@GetMapping("/past")
    public ResponseEntity<List<BookingResponseDTO>> getPastBookings(
            Authentication authentication) {

        return ResponseEntity.ok(
                bookingService.getPastBookings(authentication)
        );
    }
	
	@DeleteMapping("/cancel/{bookingId}")
	public ResponseEntity<String> cancelBooking(
	        @PathVariable Long bookingId,
	        Authentication authentication) {

	    bookingService.cancelBooking(bookingId, authentication);
	    return ResponseEntity.ok("Booking cancelled successfully");
	}
}
