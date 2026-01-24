package com.app.TurfBookingApplication.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.app.TurfBookingApplication.dto.BookingRequestDTO;
import com.app.TurfBookingApplication.dto.BookingResponseDTO;

public interface BookingService {
	BookingResponseDTO bookTurf(BookingRequestDTO request, Authentication authentication);

	List<BookingResponseDTO> getPastBookings(Authentication authentication);

	void cancelBooking(Long bookingId, Authentication authentication);
}
