package com.app.TurfBookingApplication.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.app.TurfBookingApplication.enums.BookingStatus;
import com.app.TurfBookingApplication.enums.BookingType;
import com.app.TurfBookingApplication.enums.CancelledBy;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User client;

	@ManyToOne
	private Turf turf;

	private LocalDate startDate;
	private LocalDate endDate;
	private LocalTime startTime;
	private LocalTime endTime;

	@Enumerated(EnumType.STRING)
	private BookingType bookingType;

	private Integer totalHours;

	private Double totalAmount;
	private Double advanceAmount;

	@Enumerated(EnumType.STRING)
	private BookingStatus status;

	@Enumerated(EnumType.STRING)
	private CancelledBy cancelledBy;

	private Integer totalDays;

	private Double basePrice;
	private Double accessoriesTotal;

	private Double discountAmount;
}
